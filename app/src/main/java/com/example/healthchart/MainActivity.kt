package com.example.healthchart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.healthchart.ui.theme.HealthchartTheme

class MainActivity : ComponentActivity() {

    val sampleData = listOf(10f, 25f, 15f, 30f, 20f, 35f, 35f)
    val xLabels = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HealthchartTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    rememberScrollState().let { scrollState ->
                        Column(
                            modifier = Modifier
                                .verticalScroll(scrollState)
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
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
                        }
                    }
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