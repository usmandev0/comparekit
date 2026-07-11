package com.usmandev.comparekit.beforeafterslider

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntSize

/**
 * Attaches horizontal drag handling that updates [state] based on drag delta
 * relative to [sizeProvider]'s width.
 *
 * @param state the slider state whose progress is updated during drag.
 * @param sizeProvider supplies the current layout size used to convert
 * pixel deltas into progress fractions.
 */
internal fun Modifier.sliderDragGesture(
    state: SliderState,
    sizeProvider: () -> IntSize,
): Modifier = pointerInput(state) {
    detectDragGestures { change, dragAmount ->
        change.consume()
        val width = sizeProvider().width
        if (width <= 0) return@detectDragGestures
        val nextProgress = state.progress + (dragAmount.x / width.toFloat())
        state.updateProgress(nextProgress)
    }
}
