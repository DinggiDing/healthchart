package com.example.healthchart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun FullPieChart(
    modifier: Modifier = Modifier,
    data: Map<String, Float>,
    title: String = "Pie Chart Example",
    colors: List<Color> = listOf(
        Color(0xFF3F51B5),
        Color(0xFF4CAF50),
        Color(0xFFFF9800),
        Color(0xFFF44336),
        Color(0xFF9C27B0),
        Color(0xFF00BCD4)
    )
) {
    if (data.isEmpty()) return

    val total = data.values.sum()
    if (total <= 0) return

    Column(modifier = modifier.padding(16.dp)) {
        Text(text = title, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val chartRadius = minOf(size.width, size.height) * 0.35f
                val center = Offset(size.width * 0.4f, size.height * 0.5f)

                var startAngle = 0f
                val dataList = data.toList()

                // Draw pie slices
                dataList.forEachIndexed { index, (label, value) ->
                    val sliceAngle = (value / total) * 360f
                    var sliceColor = colors[index % colors.size]

                    drawArc(
                        color = sliceColor,
                        startAngle = startAngle,
                        sweepAngle = sliceAngle,
                        useCenter = true,
                        topLeft = Offset(
                            center.x - chartRadius,
                            center.y - chartRadius
                        ),
                        size = Size(chartRadius * 2, chartRadius * 2)
                    )

                    // Draw percentage labels on slices (Only show label if slice is big enough)
                    val percentage = (value / total * 100).toInt()
                    if (percentage >= 5) {
                        val labelAngle = Math.toRadians((startAngle + sliceAngle / 2).toDouble())
                        val labelRadius = chartRadius * 0.7f
                        val labelX = center.x + (labelRadius * cos(labelAngle)).toFloat()
                        val labelY = center.y + (labelRadius * sin(labelAngle)).toFloat()

                        drawContext.canvas.nativeCanvas.drawText(
                            "$percentage%",
                            labelX,
                            labelY,
                            android.graphics.Paint().apply {
                                color = android.graphics.Color.WHITE
                                textSize = 32f
                                textAlign = android.graphics.Paint.Align.CENTER
                                isFakeBoldText = true
                                setShadowLayer(4f, 2f, 2f, android.graphics.Color.BLACK)
                            }
                        )
                    }

                    startAngle += sliceAngle
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Legend
        Column {
            data.toList().forEachIndexed { index, (label, value) ->
                val percentage = (value / total * 100).toInt()
                Row(
                    modifier = Modifier.padding(vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.size(16.dp),
                        shape = CircleShape,
                        color = colors[index % colors.size]
                    ) {}
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "$label: $percentage%",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FullPieChartPreview() {
    val sampleData = mapOf(
        "Carbs" to 8f,
        "Fat" to 59f,
        "Protein" to 33f
    )
    FullPieChart(
        data = sampleData,
        title = "Diet Nutrient Distribution"
    )
}
