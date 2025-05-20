package com.example.healthchart

import android.graphics.Canvas
import android.graphics.Rect

// 1. ChartEngine 인터페이스 정의
typealias Bounds = Rect
interface ChartEngine {
    fun layout(bounds: Bounds, data: List<DataPoint>)
    fun draw(canvas: Canvas)
    fun onGesture(event: GestureEvent)
}

// 2. DSL Config for LineChart (Builder + Fluent)
class LineChartConfig internal constructor(
    val data: List<DataPoint>,
    val xLabel: String?,
    val yLabel: String?,
    val enableZoom: Boolean,
    val showPoints: Boolean
) {
    class Builder(private val data: List<DataPoint>) {
        private var xLabel: String? = null
        private var yLabel: String? = null
        private var enableZoom = false
        private var showPoints = false

        fun xAxis(label: String) = apply { this.xLabel = label }
        fun yAxis(label: String) = apply { this.yLabel = label }
        fun zoomable(enabled: Boolean) = apply { this.enableZoom = enabled }
        fun showPoints(enabled: Boolean) = apply { this.showPoints = enabled }
        fun build(): LineChartConfig = LineChartConfig(data, xLabel, yLabel, enableZoom, showPoints)
    }
}

// 3. Engine 구현: DSL Config를 기반으로
class LineChartEngine(private val config: LineChartConfig) : ChartEngine {
    private var screenPoints: List<Pair<Float, Float>> = emptyList()
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 4f
    }
    private val pointPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        strokeWidth = 8f
    }

    override fun layout(bounds: Bounds, data: List<DataPoint>) {
        if (data.isEmpty()) { screenPoints = emptyList(); return }
        val minX = data.first().timestamp.toFloat()
        val maxX = data.last().timestamp.toFloat()
        val minY = data.minOf { it.value }
        val maxY = data.maxOf { it.value }
        val width = bounds.width().toFloat()
        val height = bounds.height().toFloat()

        screenPoints = data.map { pt ->
            val x = (pt.timestamp - minX) / (maxX - minX) * width + bounds.left
            val y = bounds.bottom - (pt.value - minY) / (maxY - minY) * height
            x to y
        }
    }

    override fun draw(canvas: Canvas) {
        if (screenPoints.size < 2) return
        // 선 그리기
        val path = Path().apply {
            val (fx, fy) = screenPoints.first()
            moveTo(fx, fy)
            screenPoints.drop(1).forEach { (x, y) -> lineTo(x, y) }
        }
        canvas.drawPath(path, linePaint)

        // 옵션: showPoints
        if (config.showPoints) {
            screenPoints.forEach { (x, y) ->
                canvas.drawCircle(x, y, 6f, pointPaint)
            }
        }
    }

    override fun onGesture(event: GestureEvent) {
        // TODO: pinch-zoom, pan 처리 후 layout 재호출
    }
}

// 4. Android View 래퍼: Fluent Interface 추가
class ChartView(context: Context) : View(context) {
    private var engine: ChartEngine? = null
    private var data: List<DataPoint> = emptyList()

    fun setEngine(engine: ChartEngine): ChartView = apply {
        this.engine = engine
        requestLayout()
    }

    fun updateData(data: List<DataPoint>): ChartView = apply {
        this.data = data
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        engine?.layout(Rect(0, 0, width, height), data)
        engine?.draw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        engine?.onGesture(GestureEvent.fromMotionEvent(event))
        invalidate()
        return true
    }
}

// 5. Compose 래퍼: DSL + Engine + Fluent ChartView
@Composable
fun LineChart(
    data: List<DataPoint>,
    modifier: Modifier = Modifier,
    configBuilder: LineChartConfig.Builder.() -> Unit = {}
) {
    val config = remember(data, configBuilder) {
        LineChartConfig.Builder(data).apply(configBuilder).build()
    }
    val engine = remember(config) { LineChartEngine(config) }

    AndroidView(
        modifier = modifier,
        factory = { ctx -> ChartView(ctx).setEngine(engine) },
        update = { view -> view.updateData(config.data) }
    )
}

// 6. 사용 예시: Builder + Fluent Interface 조합
fun setupChartView(chartView: ChartView, data: List<DataPoint>) {
    chartView
        .setEngine(
            LineChartEngine(
                LineChartConfig.Builder(data)
                    .xAxis("Time")
                    .yAxis("Steps")
                    .zoomable(true)
                    .showPoints(true)
                    .build()
            )
        )
        .updateData(data)
}

@Composable
fun DashboardScreen() {
    val stepsData = remember { sampleStepsData() }
    LineChart(
        data = stepsData,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        xAxis("Time")
        yAxis("Steps")
        zoomable(true)
        showPoints(true)
    }
}