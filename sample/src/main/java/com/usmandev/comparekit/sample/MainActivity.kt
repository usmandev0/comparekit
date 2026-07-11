package com.usmandev.comparekit.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.usmandev.comparekit.beforeafterslider.BeforeAfterSlider
import com.usmandev.comparekit.sample.ui.theme.CompareKitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CompareKitTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    BeforeAfterSliderDemo(
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}

@Composable
fun BeforeAfterSliderDemo(modifier: Modifier = Modifier) {
    BeforeAfterSlider(
        before = painterResource(id = R.drawable.before),
        after = painterResource(id = R.drawable.after),
        modifier = modifier
            .fillMaxWidth()
            .height(320.dp),
    )
}

@Preview(showBackground = true)
@Composable
fun BeforeAfterSliderPreview() {
    CompareKitTheme {
        BeforeAfterSliderDemo()
    }
}
