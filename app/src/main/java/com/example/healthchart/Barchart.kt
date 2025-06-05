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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun FullBarChart(
    modifier: Modifier = Modifier,
    data: List<Float>,
    xLabels: List<String>,
    yLabel: String = "Value",
    xLabel: String = "Time",
    title: String = "Bar Chart Example",
    lineColor: Color = Color(0xFF3F51B5)
){
    if (data.isEmpty() || data.size != xLabels.size) return

    Column(modifier = modifier.padding(16.dp)) {
        Text(text = title, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val chartHeight = size.height * 0.9f
                val chartWidth = size.width * 0.9f
                val originX = 60f
                val originY = chartHeight - 40f
                // val originX = size.width * 0.05f
                // val originY = size.height * 0.9f
                val barWidth = chartWidth / xLabels.size / 2

                val maxVal = data.maxOrNull() ?: 1f
                val minVal = 0f
                val step = (maxVal - minVal) / 5

                // Draw grid lines and Y axis labels
                for (i in 0..5) {
                    val yVal = minVal + i * step
                    val y = chartHeight - ((yVal - minVal) / (maxVal - minVal)) * chartHeight

                    drawLine(
                        color = Color.LightGray,
                        start = Offset(originX, y),
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

                // Draw X axis labels
                xLabels.forEachIndexed { i, label ->
                    val x = originX + i * barWidth * 2 + barWidth
                    drawContext.canvas.nativeCanvas.drawText(
                        label,
                        x,
                        chartHeight + 30f,
                        android.graphics.Paint().apply {
                            color = android.graphics.Color.BLACK
                            textSize = 28f
                            textAlign = android.graphics.Paint.Align.CENTER
                        }
                    )
                }
                // X/Y Axis Lines
                drawLine(Color.Black, Offset(originX, chartHeight), Offset(originX, 0f), 2f)
                drawLine(Color.Black, Offset(originX, chartHeight), Offset(originX+chartWidth, chartHeight), 2f)

                // Draw bars
                for (i in data.indices) {
                    val barX = originX + i * barWidth * 2 + barWidth / 2
                    val barHeight = ((data[i] - minVal) / (maxVal - minVal)) * chartHeight
                    drawRect(
                        color = lineColor,
                        topLeft = Offset(barX, chartHeight - barHeight),
                        size = Size(barWidth, barHeight)
                    )
                }


            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FullBarChartPreview() {
    val sampleData = listOf(10f, 25f, 15f, 30f, 20f, 35f, 35f)
    val xLabels = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    FullBarChart(
        data = sampleData,
        xLabels = xLabels,
        title = "Weekly Activity",
        xLabel = "Day",
        yLabel = "Steps"
    )
}