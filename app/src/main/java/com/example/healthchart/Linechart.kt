package com.example.healthchart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun FullLineChart(
    modifier: Modifier = Modifier,
    data: List<Float>,
    xLabels: List<String>,
    yLabel: String = "Value",
    xLabel: String = "Time",
    title: String = "Line Chart Example",
    lineColor: Color = Color(0xFF3F51B5)
) {
    if (data.isEmpty() || data.size != xLabels.size) return

    Column(modifier = modifier.padding(16.dp)) {
        Text(text = title, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        Box(modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val paddingX = 60f
                val paddingY = 40f
                val chartWidth = size.width - paddingX
                val chartHeight = size.height - paddingY

                val maxVal = data.maxOrNull() ?: 1f
//                val minVal = data.minOrNull() ?: 0f
                val minVal = 0f
                val step = (maxVal - minVal) / 5

                val pointSpacing = chartWidth / (data.size - 1)

                // Grid Lines & Y Labels
                for (i in 0..5) {
                    val yVal = minVal + i * step
                    val y = chartHeight - ((yVal - minVal) / (maxVal - minVal)) * chartHeight

                    drawLine(
                        color = Color.LightGray,
                        start = Offset(paddingX, y),
                        end = Offset(size.width, y),
                        strokeWidth = 1f
                    )

                    drawContext.canvas.nativeCanvas.drawText(
                        "%.0f".format(yVal),
                        10f,
                        y + 10f,
                        android.graphics.Paint().apply {
                            color = android.graphics.Color.BLACK
                            textSize = 28f
                        }
                    )
                }

                // X축 Labels
                xLabels.forEachIndexed { i, label ->
                    val x = paddingX + i * pointSpacing
                    drawContext.canvas.nativeCanvas.drawText(
                        label,
                        x - 20f,
                        size.height,
                        android.graphics.Paint().apply {
                            color = android.graphics.Color.BLACK
                            textSize = 28f
                        }
                    )
                }

                // Line Path
                val points = data.mapIndexed { index, value ->
                    val x = paddingX + index * pointSpacing
                    val y = chartHeight - ((value - minVal) / (maxVal - minVal)) * chartHeight
                    Offset(x, y)
                }

                val path = Path().apply {
                    moveTo(points.first().x, points.first().y)
                    points.drop(1).forEach { lineTo(it.x, it.y) }
                }

                drawPath(path, color = lineColor, style = Stroke(4f))

                // Points & Labels
                points.forEachIndexed { i, point ->
                    drawCircle(lineColor, 8f, center = point)
                    drawCircle(Color.White, 4f, center = point)

                    drawContext.canvas.nativeCanvas.drawText(
                        data[i].toInt().toString(),
                        point.x,
                        point.y - 12f,
                        android.graphics.Paint().apply {
                            color = android.graphics.Color.BLACK
                            textSize = 24f
                        }
                    )
                }

                // X/Y Axis Lines
                drawLine(Color.Black, Offset(paddingX, 0f), Offset(paddingX, chartHeight), 2f)
                drawLine(Color.Black, Offset(paddingX, chartHeight), Offset(size.width, chartHeight), 2f)
            }
        }

        Spacer(modifier = Modifier.height(4.dp))
        Text("X축: $xLabel", style = MaterialTheme.typography.labelMedium)
        Text("Y축: $yLabel", style = MaterialTheme.typography.labelMedium)
    }
}

@Preview(showBackground = true)
@Composable
fun FullLineChartPreview() {
    val sampleData = listOf(10f, 25f, 15f, 30f, 20f, 35f, 35f)
    val xLabels = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    FullLineChart(
        data = sampleData,
        xLabels = xLabels,
        title = "Weekly Activity",
        xLabel = "Day",
        yLabel = "Steps"
    )
}