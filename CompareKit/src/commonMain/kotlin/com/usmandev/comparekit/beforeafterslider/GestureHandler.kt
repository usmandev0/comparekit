package com.usmandev.comparekit.beforeafterslider

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

/**
 * Attaches horizontal drag, double-tap reset, and long-press handling to a
 * [BeforeAfterSlider].
 *
 * @param state the slider state whose progress is updated by gestures.
 * @param sizeProvider supplies the current layout size used to convert
 * pixel deltas into progress fractions.
 * @param enabled when `false`, all gestures are ignored.
 * @param doubleTapToReset when `true`, a double tap animates [state] back to
 * [defaultSliderPosition].
 * @param defaultSliderPosition target progress used when resetting via double tap.
 * @param onDoubleTap optional callback invoked after a double tap is detected.
 * @param onLongPress optional callback invoked when a long press is detected.
 */
internal fun Modifier.sliderGestures(
    state: SliderState,
    sizeProvider: () -> IntSize,
    enabled: Boolean = true,
    doubleTapToReset: Boolean = true,
    defaultSliderPosition: Float = CompareKitDefaults.SliderPosition,
    onDoubleTap: (() -> Unit)? = null,
    onLongPress: (() -> Unit)? = null,
): Modifier {
    if (!enabled) return this

    val resetTarget = defaultSliderPosition.coerceIn(SliderState.MinProgress, SliderState.MaxProgress)

    return pointerInput(state, doubleTapToReset, resetTarget) {
        coroutineScope {
            val progressAnimatable = Animatable(state.progress)
            var resetAnimationJob: Job? = null

            launch {
                detectDragGestures(
                    onDragStart = {
                        resetAnimationJob?.cancel()
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        val width = sizeProvider().width
                        if (width <= 0) return@detectDragGestures
                        val nextProgress = state.progress + (dragAmount.x / width.toFloat())
                        state.updateProgress(nextProgress)
                    },
                )
            }

            launch {
                detectTapGestures(
                    onDoubleTap = {
                        if (doubleTapToReset) {
                            resetAnimationJob?.cancel()
                            resetAnimationJob = this@coroutineScope.launch {
                                progressAnimatable.snapTo(state.progress)
                                progressAnimatable.animateTo(
                                    targetValue = resetTarget,
                                    animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                                ) {
                                    state.updateProgress(value)
                                }
                            }
                        }
                        onDoubleTap?.invoke()
                    },
                    onLongPress = {
                        onLongPress?.invoke()
                    },
                )
            }
        }
    }
}
