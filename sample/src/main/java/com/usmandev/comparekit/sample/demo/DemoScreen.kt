package com.usmandev.comparekit.sample.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.usmandev.comparekit.beforeafterslider.BeforeAfterSlider
import com.usmandev.comparekit.beforeafterslider.CompareLabel
import com.usmandev.comparekit.beforeafterslider.CompareLabelAlignment
import com.usmandev.comparekit.beforeafterslider.rememberCompareSliderState

/**
 * Interactive showcase screen that demonstrates every public CompareKit feature.
 */
@Composable
fun DemoScreen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val beforePainter = rememberDemoBeforePainter()
    val afterPainter = rememberDemoAfterPainter()
    val sharedSliderState = rememberCompareSliderState()
    val demoState = rememberDemoState(sliderState = sharedSliderState)
    val basicSliderState = rememberCompareSliderState()

    LaunchedEffect(demoState.gestureMessage) {
        val message = demoState.gestureMessage ?: return@LaunchedEffect
        snackbarHostState.showSnackbar(message)
        demoState.gestureMessage = null
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(DemoContentPadding),
        verticalArrangement = Arrangement.spacedBy(DemoSectionSpacing),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "CompareKit Demo",
                style = MaterialTheme.typography.headlineMedium,
            )
            Text(
                text = "Explore customization, labels, gestures, and live controls.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        DemoSection(
            title = "1. Basic Slider",
            description = "A before/after comparison slider using library defaults.",
        ) {
            BeforeAfterSlider(
                before = beforePainter,
                after = afterPainter,
                state = basicSliderState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(DemoSliderHeight),
            )
        }

        DemoSection(
            title = "2. Customization",
            description = "Tune divider, thumb, corners, and visibility. Changes apply to all connected sliders.",
        ) {
            ConnectedBeforeAfterSlider(
                demoState = demoState,
                before = beforePainter,
                after = afterPainter,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(DemoSliderHeight),
            )
            CustomizationControls(demoState = demoState)
        }

        DemoSection(
            title = "3. Labels",
            description = "Default labels, custom composables, hidden labels, and alignment options.",
        ) {
            ConnectedBeforeAfterSlider(
                demoState = demoState,
                before = beforePainter,
                after = afterPainter,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(DemoSliderHeight),
            )
            LabelControls(demoState = demoState)
            LabelExamplesRow(
                beforePainter = beforePainter,
                afterPainter = afterPainter,
            )
        }

        DemoSection(
            title = "4. Gestures",
            description = "Drag to compare, double-tap to reset, and long-press for feedback.",
        ) {
            ConnectedBeforeAfterSlider(
                demoState = demoState,
                before = beforePainter,
                after = afterPainter,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(DemoSliderHeight),
                onDoubleTap = {
                    demoState.gestureMessage = "Double tap — slider reset to center"
                },
                onLongPress = {
                    demoState.gestureMessage = "Long press detected"
                },
            )
            Text(
                text = "Position: ${demoState.sliderPositionPercent}%",
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = "Double-tap to reset • Long-press for a snackbar",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        DemoSection(
            title = "5. Orientation",
            description = "Horizontal comparison is available today. Vertical orientation is planned for a future release.",
        ) {
            OrientationExamples(
                demoState = demoState,
                beforePainter = beforePainter,
                afterPainter = afterPainter,
            )
        }

        DemoSection(
            title = "6. Live Controls",
            description = "Adjust shared settings that update every connected slider simultaneously.",
        ) {
            ConnectedBeforeAfterSlider(
                demoState = demoState,
                before = beforePainter,
                after = afterPainter,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(DemoSliderHeight),
            )
            LiveControlsPanel(demoState = demoState)
        }
    }
}

@Composable
private fun LabelExamplesRow(
    beforePainter: Painter,
    afterPainter: Painter,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "Quick examples",
            style = MaterialTheme.typography.labelLarge,
        )
        BeforeAfterSlider(
            before = beforePainter,
            after = afterPainter,
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
            cornerRadius = 8.dp,
        )
        BeforeAfterSlider(
            before = beforePainter,
            after = afterPainter,
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
            cornerRadius = 8.dp,
            beforeLabel = { CompareLabel(text = "Original") },
            afterLabel = { CompareLabel(text = "Edited") },
            labelAlignment = CompareLabelAlignment.BottomStart,
        )
        BeforeAfterSlider(
            before = beforePainter,
            after = afterPainter,
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
            cornerRadius = 8.dp,
            showBeforeLabel = false,
            showAfterLabel = false,
        )
    }
}

@Composable
private fun OrientationExamples(
    demoState: DemoState,
    beforePainter: Painter,
    afterPainter: Painter,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "Horizontal",
            style = MaterialTheme.typography.titleSmall,
        )
        ConnectedBeforeAfterSlider(
            demoState = demoState,
            before = beforePainter,
            after = afterPainter,
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp),
        )
        Text(
            text = "Vertical — coming soon",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = "CompareKit currently supports horizontal before/after comparison. A vertical slider API will be added in a future version.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
