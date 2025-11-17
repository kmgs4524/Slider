package com.york.slider

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.york.slider.component.ThumbType
import com.york.slider.ui.theme.SliderTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SliderTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.fillMaxSize().background(Color.White),
                        contentAlignment = Alignment.Center) {
                        RangeSlider(
                            label = listOf("$", "$$", "$$$", "$$$$", "$$$$$"),
                            steps = 5,
                            thumbStepMap = mapOf(
                                ThumbType.LEFT to 0,
                                ThumbType.RIGHT to 4
                            ),
                            onRangeChanged = { range ->
                                // Handle range change
                                Toast.makeText(this@MainActivity, "Selected range: ${range.first} to ${range.last}", Toast.LENGTH_SHORT).show()
                            }
                        )
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
    SliderTheme {
        Greeting("Android")
    }
}