package com.example.healthchart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.healthchart.ui.theme.HealthchartTheme

class MainActivity : ComponentActivity() {

    val sampleData = listOf(10f, 25f, 15f, 30f, 20f, 35f, 35f)
    val xLabels = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    val dietData = mapOf(
        "Carbs" to 8f,
        "Protein" to 59f,
        "Fat" to 33f
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HealthchartTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                    FullBarChart(
                        data = sampleData,
                        xLabels = xLabels,
                        title = "Weekly Activity",
                        xLabel = "Day",
                        yLabel = "Steps"
                    )
                    FullLineChart(
                        data = sampleData,
                        xLabels = xLabels,
                        title = "Weekly Activity",
                        xLabel = "Day",
                        yLabel = "Steps"
                    )
                    FullPieChart(
                        data = dietData,
                        title = "Weekly Activity"
                    )
                    FullScatterPlot(
                        data = sampleData,
                        xLabels = xLabels,
                        title = "Weekly Activity",
                        xLabel = "Day",
                        yLabel = "Steps"
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HealthchartTheme {
        Greeting("Android")
    }
}