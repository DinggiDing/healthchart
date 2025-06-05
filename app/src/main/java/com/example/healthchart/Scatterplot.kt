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
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun FullScatterPlot(
    modifier: Modifier = Modifier,
    data: List<Float>,
    xLabels: List<String>,
    yLabel: String = "Value",
    xLabel: String = "Time",
    title: String = "Scatter Plot Example",
    dotColor: Color = Color(0xFF3F51B5),
    dotRadius: Float = 8f
) {
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
                val chartPaddingX = 60f
                val chartPaddingY = chartHeight - 40f
                val pointSpacing = chartWidth / xLabels.size

                val maxVal = data.maxOrNull() ?: 1f
                val minVal = 0f
                val step = (maxVal - minVal) / 5

                // Draw grid lines and Y-axis labels
                for (i in 0..5) {
                    val yVal = minVal + i * step
                    val y = chartHeight - ((yVal - minVal) / (maxVal - minVal)) * chartHeight

                    drawLine(
                        color = Color.LightGray,
                        start = Offset(chartPaddingX, y),
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

                // Draw X-axis labels
                xLabels.forEachIndexed { i, label ->
                    val x = chartPaddingX + i * pointSpacing + pointSpacing / 2
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

                // Draw X/Y axis lines
                drawLine(Color.Black, Offset(chartPaddingX, chartHeight), Offset(chartPaddingX, 0f), 2f)
                drawLine(Color.Black, Offset(chartPaddingX, chartHeight), Offset(chartPaddingX + chartWidth, chartHeight), 2f)

                // Draw scatter plot dots
                for (i in data.indices) {
                    val dotX = chartPaddingX + i * pointSpacing + pointSpacing / 2
                    val dotY = chartHeight - ((data[i] - minVal) / (maxVal - minVal)) * chartHeight

                    // Draw outer dot (main color)
                    drawCircle(
                        color = dotColor,
                        radius = dotRadius,
                        center = Offset(dotX, dotY)
                    )

                    // Draw inner dot (white center for better visibility)
                    drawCircle(
                        color = Color.White,
                        radius = dotRadius * 0.4f,
                        center = Offset(dotX, dotY)
                    )

                    // Draw value labels on dots
                    drawContext.canvas.nativeCanvas.drawText(
                        data[i].toInt().toString(),
                        dotX,
                        dotY - dotRadius - 8f,
                        android.graphics.Paint().apply {
                            color = android.graphics.Color.BLACK
                            textSize = 24f
                            textAlign = android.graphics.Paint.Align.CENTER
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))
        Text("X-axis: $xLabel", style = MaterialTheme.typography.labelMedium)
        Text("Y-axis: $yLabel", style = MaterialTheme.typography.labelMedium)
    }
}

@Preview(showBackground = true)
@Composable
fun FullScatterPlotPreview() {
    val sampleData = listOf(10f, 25f, 15f, 30f, 20f, 35f, 35f)
    val xLabels = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    FullScatterPlot(
        data = sampleData,
        xLabels = xLabels,
        title = "Weekly Activity",
        xLabel = "Day",
        yLabel = "Steps"
    )
}