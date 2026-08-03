package com.usmandev.comparekit.app.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.height
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.usmandev.comparekit.beforeafterslider.BeforeAfterSlider
import com.usmandev.comparekit.beforeafterslider.CompareAnimationMode
import com.usmandev.comparekit.beforeafterslider.CompareKitDefaults
import com.usmandev.comparekit.beforeafterslider.rememberCompareSliderState

internal val DemoAutoScrubDurationRange = 500..4000

/**
 * Interactive auto-scrub demo with mode, speed, and pause-on-interaction controls.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AutoAnimationDemo(
    beforePainter: Painter,
    afterPainter: Painter,
    modifier: Modifier = Modifier,
) {
    var animationMode by remember { mutableStateOf(CompareAnimationMode.PingPong) }
    var autoScrubDurationMillis by remember { mutableIntStateOf(CompareKitDefaults.AutoScrubDurationMillis) }
    var pauseOnInteraction by remember { mutableStateOf(CompareKitDefaults.PauseAutoScrubOnInteraction) }
    val sliderState = rememberCompareSliderState()

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        BeforeAfterSlider(
            before = beforePainter,
            after = afterPainter,
            state = sliderState,
            modifier = Modifier
                .fillMaxWidth()
                .height(DemoSliderHeight),
            cornerRadius = 12.dp,
            animationMode = animationMode,
            autoScrubDurationMillis = autoScrubDurationMillis,
            pauseAutoScrubOnInteraction = pauseOnInteraction,
        )

        Text(
            text = "Position: ${(sliderState.progress * 100f).toInt()}%",
            style = MaterialTheme.typography.titleMedium,
        )

        Text(
            text = "Animation mode",
            style = MaterialTheme.typography.labelLarge,
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            CompareAnimationMode.entries.forEach { mode ->
                FilterChip(
                    selected = animationMode == mode,
                    onClick = { animationMode = mode },
                    label = { Text(mode.name) },
                )
            }
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Speed (ms per leg)",
                    style = MaterialTheme.typography.labelLarge,
                )
                Text(
                    text = "${autoScrubDurationMillis}ms",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Slider(
                value = autoScrubDurationMillis.toFloat(),
                onValueChange = { autoScrubDurationMillis = it.toInt() },
                valueRange = DemoAutoScrubDurationRange.first.toFloat()..DemoAutoScrubDurationRange.last.toFloat(),
                steps = ((DemoAutoScrubDurationRange.last - DemoAutoScrubDurationRange.first) / 250) - 1,
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Pause on drag",
                style = MaterialTheme.typography.bodyLarge,
            )
            Switch(
                checked = pauseOnInteraction,
                onCheckedChange = { pauseOnInteraction = it },
            )
        }

        Text(
            text = "Drag to pause PingPong • RevealOnce animates once to 100%",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
