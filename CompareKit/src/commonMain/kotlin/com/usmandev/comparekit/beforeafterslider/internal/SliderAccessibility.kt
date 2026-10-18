package com.usmandev.comparekit.beforeafterslider.internal

import androidx.compose.foundation.focusable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.setProgress
import androidx.compose.ui.semantics.stateDescription
import com.usmandev.comparekit.beforeafterslider.SliderOrientation
import com.usmandev.comparekit.beforeafterslider.SliderState
import kotlin.math.roundToInt

internal fun resolveCompareSliderContentDescription(
    contentDescription: String,
    beforeContentDescription: String?,
    afterContentDescription: String?,
): String {
    val parts = buildList {
        add(contentDescription)
        if (!beforeContentDescription.isNullOrBlank()) {
            add("Before: $beforeContentDescription")
        }
        if (!afterContentDescription.isNullOrBlank()) {
            add("After: $afterContentDescription")
        }
    }
    return parts.joinToString(". ")
}

internal fun progressStateDescription(progress: Float): String {
    val percent = (progress.coerceIn(SliderState.MinProgress, SliderState.MaxProgress) * 100f)
        .roundToInt()
    return "$percent percent after"
}

/**
 * Exposes the slider with accessible progress semantics and [setProgress].
 *
 * Compose Multiplatform commonMain does not expose Role.Slider yet, so role is omitted;
 * [ProgressBarRangeInfo] + [setProgress] convey the interactive range control.
 */
internal fun Modifier.compareSliderSemantics(
    state: SliderState,
    contentDescription: String,
    enabled: Boolean,
): Modifier = semantics(mergeDescendants = true) {
    this.contentDescription = contentDescription
    this.stateDescription = progressStateDescription(state.progress)
    this.progressBarRangeInfo = ProgressBarRangeInfo(
        current = state.progress,
        range = SliderState.MinProgress..SliderState.MaxProgress,
    )
    if (!enabled) {
        disabled()
    } else {
        setProgress { target ->
            state.updateProgress(target)
            true
        }
    }
}

/**
 * Keyboard navigation for the slider: arrows adjust by [progressStep], Home/End jump.
 */
internal fun Modifier.compareSliderKeyboard(
    state: SliderState,
    orientation: SliderOrientation,
    enabled: Boolean,
    progressStep: Float,
): Modifier {
    if (!enabled) return this

    val step = progressStep.coerceAtLeast(0.001f)
    return focusable().onKeyEvent { event ->
        if (event.type != KeyEventType.KeyDown) return@onKeyEvent false

        when (event.key) {
            Key.DirectionLeft -> {
                if (orientation == SliderOrientation.Horizontal) {
                    state.updateProgress(state.progress - step)
                    true
                } else {
                    false
                }
            }

            Key.DirectionRight -> {
                if (orientation == SliderOrientation.Horizontal) {
                    state.updateProgress(state.progress + step)
                    true
                } else {
                    false
                }
            }

            Key.DirectionUp -> {
                if (orientation == SliderOrientation.Vertical) {
                    state.updateProgress(state.progress - step)
                    true
                } else {
                    false
                }
            }

            Key.DirectionDown -> {
                if (orientation == SliderOrientation.Vertical) {
                    state.updateProgress(state.progress + step)
                    true
                } else {
                    false
                }
            }

            Key.MoveHome -> {
                state.updateProgress(SliderState.MinProgress)
                true
            }

            Key.MoveEnd -> {
                state.updateProgress(SliderState.MaxProgress)
                true
            }

            else -> false
        }
    }
}
