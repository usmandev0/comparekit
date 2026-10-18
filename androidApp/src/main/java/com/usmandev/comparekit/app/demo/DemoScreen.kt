package com.usmandev.comparekit.app.demo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.usmandev.comparekit.app.R
import com.usmandev.comparekit.beforeafterslider.BeforeAfterSlider
import com.usmandev.comparekit.beforeafterslider.CompareLabel
import com.usmandev.comparekit.beforeafterslider.CompareLabelAlignment
import com.usmandev.comparekit.beforeafterslider.SliderOrientation
import com.usmandev.comparekit.beforeafterslider.rememberCompareSliderState
import com.usmandev.comparekit.beforeafterslider.rememberCompareZoomState
import kotlin.math.roundToInt

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
    val coilBefore = rememberDemoCoilBeforeSource()
    val coilAfter = rememberDemoCoilAfterSource()
    val thumbIcon = rememberDemoThumbIcon()
    val coilSliderState = rememberCompareSliderState()
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
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Image(
                painter = painterResource(id = R.drawable.comparekit_logo),
                contentDescription = "CompareKit logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth(0.72f)
                    .height(120.dp),
            )
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

        DemoWhatsNewCard()

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
            title = "2. Coil & Custom Thumb",
            description = "Load remote images with Coil and show an icon or image on the slider thumb.",
        ) {
            BeforeAfterSlider(
                before = coilBefore,
                after = coilAfter,
                state = coilSliderState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(DemoSliderHeight),
                cornerRadius = 12.dp,
                thumbPainter = thumbIcon,
                thumbSize = 40.dp,
                thumbIconPadding = 3.dp,
            )
            Text(
                text = "Images load from URLs via CompareImageSource + Coil. Pass any Coil model: URL, Uri, File, drawable ID, or bitmap.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        DemoSection(
            title = "3. Customization",
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
            title = "4. Labels",
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
            title = "5. Gestures",
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
            title = "6. Orientation",
            description = "Compare images horizontally (left/right) or vertically (top/bottom).",
        ) {
            OrientationExamples(
                beforePainter = beforePainter,
                afterPainter = afterPainter,
            )
        }

        DemoSection(
            title = "7. Live Controls",
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

        DemoSection(
            title = "8. Auto Animation",
            description = "Automatically animate the slider with PingPong or RevealOnce modes.",
        ) {
            AutoAnimationDemo(
                beforePainter = beforePainter,
                afterPainter = afterPainter,
            )
        }

        DemoSection(
            title = "9. Accessibility",
            description = "Slider semantics for TalkBack, content descriptions, and keyboard steps.",
        ) {
            val a11yState = rememberCompareSliderState()
            BeforeAfterSlider(
                before = beforePainter,
                after = afterPainter,
                state = a11yState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(DemoSliderHeight),
                cornerRadius = 12.dp,
                contentDescription = "Photo retouch comparison",
                beforeContentDescription = "Original photo",
                afterContentDescription = "Edited photo",
                progressStep = 0.05f,
            )
            Text(
                text = "TalkBack announces progress. Focus the slider and use arrow keys " +
                    "(±5%), Home, or End. Images stay decorative; descriptions are on the slider.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        DemoSection(
            title = "10. Zoom & Pan",
            description = "Pinch both images together, pan the magnified view, and keep dragging the divider to compare.",
        ) {
            val zoomSliderState = rememberCompareSliderState()
            val zoomState = rememberCompareZoomState()
            BeforeAfterSlider(
                before = beforePainter,
                after = afterPainter,
                state = zoomSliderState,
                zoomState = zoomState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(DemoSliderHeight),
                cornerRadius = 12.dp,
            )
            Text(
                text = "Zoom ${(zoomState.scale * 10f).roundToInt() / 10f}×",
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = "Pinch or scroll-wheel to zoom • Drag the image to pan when zoomed • " +
                    "Drag the divider to compare • Double-tap resets zoom, then the slider",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            OutlinedButton(
                onClick = zoomState::reset,
                enabled = zoomState.isZoomed,
            ) {
                Text("Reset zoom")
            }
        }

        DemoGlobalActions(demoState = demoState)
    }
}

@Composable
private fun DemoWhatsNewCard(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerHighest,
                shape = RoundedCornerShape(12.dp),
            )
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = "What's new (Unreleased)",
            style = MaterialTheme.typography.titleSmall,
        )
        Text(
            text = "• Synced pinch-zoom and pan: both images stay aligned while you compare\n" +
                "• Accessibility: content descriptions, progress semantics, keyboard steps\n" +
                "• Defaults reorganized under defaults/; internals under internal/",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Suppress("ParamsComparedByRef")
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

@Suppress("ParamsComparedByRef")
@Composable
private fun OrientationExamples(
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
        BeforeAfterSlider(
            before = beforePainter,
            after = afterPainter,
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp),
            cornerRadius = 8.dp,
        )
        Text(
            text = "Vertical",
            style = MaterialTheme.typography.titleSmall,
        )
        BeforeAfterSlider(
            orientation = SliderOrientation.Vertical,
            before = beforePainter,
            after = afterPainter,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            cornerRadius = 8.dp,
        )
    }
}
