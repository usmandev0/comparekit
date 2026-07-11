package com.usmandev.comparekit.sample.demo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.usmandev.comparekit.beforeafterslider.CompareKitDefaults
import com.usmandev.comparekit.beforeafterslider.CompareLabelAlignment
import com.usmandev.comparekit.beforeafterslider.SliderState

/**
 * Shared UI state that drives every connected [com.usmandev.comparekit.beforeafterslider.BeforeAfterSlider]
 * in the demo screen.
 */
@Stable
class DemoState(
    val sliderState: SliderState,
) {
    /** Divider line color applied to connected sliders. */
    var dividerColor: Color by mutableStateOf(CompareKitDefaults.DividerColor)

    /** Divider line thickness applied to connected sliders. */
    var dividerThickness: Dp by mutableStateOf(CompareKitDefaults.DividerThickness)

    /** Thumb fill color applied to connected sliders. */
    var thumbColor: Color by mutableStateOf(CompareKitDefaults.ThumbColor)

    /** Thumb size applied to connected sliders. */
    var thumbSize: Dp by mutableStateOf(CompareKitDefaults.ThumbSize)

    /** Corner radius applied to connected sliders. */
    var cornerRadius: Dp by mutableStateOf(12.dp)

    /** Whether the divider is visible on connected sliders. */
    var showDivider: Boolean by mutableStateOf(true)

    /** Whether the thumb is visible on connected sliders. */
    var showThumb: Boolean by mutableStateOf(true)

    /** Whether gestures are enabled on connected sliders. */
    var enabled: Boolean by mutableStateOf(true)

    /** Label alignment applied to connected sliders. */
    var labelAlignment: CompareLabelAlignment by mutableStateOf(CompareLabelAlignment.TopStart)

    /** Whether the before label is shown on connected sliders. */
    var showBeforeLabel: Boolean by mutableStateOf(true)

    /** Whether the after label is shown on connected sliders. */
    var showAfterLabel: Boolean by mutableStateOf(true)

    /** When `true`, connected sliders use custom label composables. */
    var useCustomLabels: Boolean by mutableStateOf(false)

    /** Latest gesture feedback message shown in the gestures section. */
    var gestureMessage: String? by mutableStateOf(null)

    /** Current slider progress as a percentage in the range `0`–`100`. */
    val sliderPositionPercent: Int
        get() = (sliderState.progress * 100f).toInt()

    /** Resets [sliderState] to the library default position. */
    fun resetPosition() {
        sliderState.updateProgress(CompareKitDefaults.SliderPosition)
    }

    /** Resets every connected control to its default value. */
    fun resetAll() {
        dividerColor = CompareKitDefaults.DividerColor
        dividerThickness = CompareKitDefaults.DividerThickness
        thumbColor = CompareKitDefaults.ThumbColor
        thumbSize = CompareKitDefaults.ThumbSize
        cornerRadius = 12.dp
        showDivider = true
        showThumb = true
        enabled = true
        labelAlignment = CompareLabelAlignment.TopStart
        showBeforeLabel = true
        showAfterLabel = true
        useCustomLabels = false
        gestureMessage = null
        resetPosition()
    }
}

/**
 * Creates and remembers a [DemoState] bound to [sliderState].
 */
@Composable
fun rememberDemoState(
    sliderState: SliderState,
): DemoState {
    return rememberSaveable(sliderState, saver = DemoStateSaver(sliderState)) {
        DemoState(sliderState = sliderState)
    }
}

/** Range and preset values used by demo controls. */
internal val DemoThumbSizeRange: ClosedRange<Dp> = 16.dp..48.dp
internal val DemoDividerThicknessRange: ClosedRange<Dp> = 1.dp..8.dp
internal val DemoCornerRadiusRange: ClosedRange<Dp> = 0.dp..24.dp
internal val DemoSliderPositionRange: ClosedFloatingPointRange<Float> = 0f..1f

internal val DemoDividerColorOptions: List<Pair<String, Color>> = listOf(
    "White" to CompareKitDefaults.DividerColor,
    "Primary" to Color(0xFF6750A4),
    "Cyan" to Color(0xFF00BCD4),
    "Amber" to Color(0xFFFFC107),
)

internal val DemoThumbColorOptions: List<Pair<String, Color>> = listOf(
    "White" to CompareKitDefaults.ThumbColor,
    "Pink" to Color(0xFFE91E63),
    "Green" to Color(0xFF4CAF50),
    "Orange" to Color(0xFFFF9800),
)

internal val DemoLabelAlignmentOptions: List<CompareLabelAlignment> = CompareLabelAlignment.entries

/**
 * [Saver] that persists user-editable [DemoState] fields across configuration changes.
 */
private fun DemoStateSaver(sliderState: SliderState): Saver<DemoState, Any> = listSaver(
    save = { state ->
        listOf(
            state.dividerColor.toArgb(),
            state.dividerThickness.value,
            state.thumbColor.toArgb(),
            state.thumbSize.value,
            state.cornerRadius.value,
            state.showDivider,
            state.showThumb,
            state.enabled,
            state.labelAlignment.name,
            state.showBeforeLabel,
            state.showAfterLabel,
            state.useCustomLabels,
            state.gestureMessage,
        )
    },
    restore = { values ->
        DemoState(sliderState = sliderState).apply {
            dividerColor = Color(values[0] as Int)
            dividerThickness = (values[1] as Float).dp
            thumbColor = Color(values[2] as Int)
            thumbSize = (values[3] as Float).dp
            cornerRadius = (values[4] as Float).dp
            showDivider = values[5] as Boolean
            showThumb = values[6] as Boolean
            enabled = values[7] as Boolean
            labelAlignment = CompareLabelAlignment.valueOf(values[8] as String)
            showBeforeLabel = values[9] as Boolean
            showAfterLabel = values[10] as Boolean
            useCustomLabels = values[11] as Boolean
            gestureMessage = values[12] as String?
        }
    },
)
