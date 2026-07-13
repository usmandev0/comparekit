package com.usmandev.comparekit.beforeafterslider

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

/**
 * Holds the reveal progress of a [BeforeAfterSlider].
 *
 * Progress is expressed as a fraction of the slider axis in the range
 * `[`[MinProgress]`,`[MaxProgress]`]`, where `0f` shows only the before image
 * and `1f` shows the after image fully.
 *
 * @param initialProgress the starting progress; values outside
 * `[`[MinProgress]`,`[MaxProgress]`]` are clamped.
 */
@Stable
class SliderState(
    initialProgress: Float = DefaultProgress,
) {
    /**
     * Current slider progress in the range `[`[MinProgress]`,`[MaxProgress]`]`.
     * Assignments outside that range are clamped.
     */
    var progress: Float by mutableFloatStateOf(initialProgress.coerceIn(MinProgress, MaxProgress))
        private set

    /**
     * Updates [progress] to [value], clamping to `[`[MinProgress]`,`[MaxProgress]`]`.
     */
    fun updateProgress(value: Float) {
        progress = value.coerceIn(MinProgress, MaxProgress)
    }

    companion object {
        /** Minimum allowed progress (before image only). */
        const val MinProgress: Float = 0f

        /** Maximum allowed progress (after image fully revealed). */
        const val MaxProgress: Float = 1f

        /** Default progress used when no initial value is provided (50%). */
        const val DefaultProgress: Float = 0.5f
    }
}

/**
 * Creates and remembers a [SliderState].
 *
 * @param initialProgress the starting progress; defaults to 50%.
 */
@Composable
fun rememberCompareSliderState(
    initialProgress: Float = SliderState.DefaultProgress,
): SliderState {
    return remember {
        SliderState(initialProgress = initialProgress)
    }
}
