package com.usmandev.comparekit.beforeafterslider.internal

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntSize
import com.usmandev.comparekit.beforeafterslider.CompareKitDefaults
import com.usmandev.comparekit.beforeafterslider.SliderOrientation
import com.usmandev.comparekit.beforeafterslider.SliderState
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

/**
 * Attaches drag, double-tap reset, and long-press handling to a before/after slider.
 */
internal fun Modifier.sliderGestures(
    state: SliderState,
    sizeProvider: () -> IntSize,
    orientation: SliderOrientation = SliderOrientation.Horizontal,
    enabled: Boolean = true,
    doubleTapToReset: Boolean = true,
    defaultSliderPosition: Float = CompareKitDefaults.SliderPosition,
    onDoubleTap: (() -> Unit)? = null,
    onLongPress: (() -> Unit)? = null,
    onInteractionStart: (() -> Unit)? = null,
    onInteractionEnd: (() -> Unit)? = null,
): Modifier {
    if (!enabled) return this

    val resetTarget = defaultSliderPosition.coerceIn(SliderState.MinProgress, SliderState.MaxProgress)

    return pointerInput(state, orientation, doubleTapToReset, resetTarget) {
        coroutineScope {
            val progressAnimatable = Animatable(state.progress)
            var resetAnimationJob: Job? = null

            launch {
                detectDragGestures(
                    onDragStart = {
                        resetAnimationJob?.cancel()
                        onInteractionStart?.invoke()
                    },
                    onDragEnd = {
                        onInteractionEnd?.invoke()
                    },
                    onDragCancel = {
                        onInteractionEnd?.invoke()
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        val primaryDimension = sliderPrimaryDimension(sizeProvider(), orientation)
                        if (primaryDimension <= 0f) return@detectDragGestures
                        val dragDelta = when (orientation) {
                            SliderOrientation.Horizontal -> dragAmount.x
                            SliderOrientation.Vertical -> dragAmount.y
                        }
                        val nextProgress = state.progress + (dragDelta / primaryDimension)
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
