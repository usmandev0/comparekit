package com.usmandev.comparekit.beforeafterslider.internal

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculateCentroid
import androidx.compose.foundation.gestures.calculateCentroidSize
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChanged
import androidx.compose.ui.unit.IntSize
import com.usmandev.comparekit.beforeafterslider.CompareKitDefaults
import com.usmandev.comparekit.beforeafterslider.CompareZoomState
import com.usmandev.comparekit.beforeafterslider.SliderOrientation
import com.usmandev.comparekit.beforeafterslider.SliderState
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.abs

/**
 * Attaches drag, optional zoom/pan, double-tap reset, and long-press handling
 * to a before/after slider.
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
    zoomEnabled: Boolean = false,
    zoomState: CompareZoomState? = null,
    zoomHitSlopPx: Float = 0f,
): Modifier {
    if (!enabled) return this

    val resetTarget = defaultSliderPosition.coerceIn(SliderState.MinProgress, SliderState.MaxProgress)
    val resolvedZoomState = zoomState.takeIf { zoomEnabled }

    val gestureModifier = pointerInput(
        state,
        orientation,
        doubleTapToReset,
        resetTarget,
        zoomEnabled,
        resolvedZoomState,
        zoomHitSlopPx,
    ) {
        coroutineScope {
            val progressAnimatable = Animatable(state.progress)
            var resetAnimationJob: Job? = null

            launch {
                detectTapGestures(
                    onDoubleTap = {
                        resetAnimationJob?.cancel()
                        resetAnimationJob = this@coroutineScope.launch {
                            val zoom = resolvedZoomState
                            if (zoom != null && zoom.isZoomed) {
                                zoom.animateReset()
                            } else if (doubleTapToReset) {
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

            launch {
                if (resolvedZoomState != null) {
                    detectSliderAndZoomGestures(
                        state = state,
                        zoomState = resolvedZoomState,
                        sizeProvider = sizeProvider,
                        orientation = orientation,
                        zoomHitSlopPx = zoomHitSlopPx,
                        onInteractionStart = onInteractionStart,
                        onInteractionEnd = onInteractionEnd,
                    )
                } else {
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
                            applySliderDrag(
                                state = state,
                                sizeProvider = sizeProvider,
                                orientation = orientation,
                                dragDelta = when (orientation) {
                                    SliderOrientation.Horizontal -> dragAmount.x
                                    SliderOrientation.Vertical -> dragAmount.y
                                },
                            )
                        },
                    )
                }
            }
        }
    }

    if (resolvedZoomState == null) {
        return gestureModifier
    }

    return gestureModifier.pointerInput(resolvedZoomState) {
        detectScrollZoomGestures(
            zoomState = resolvedZoomState,
            sizeProvider = sizeProvider,
        )
    }
}

private suspend fun PointerInputScope.detectSliderAndZoomGestures(
    state: SliderState,
    zoomState: CompareZoomState,
    sizeProvider: () -> IntSize,
    orientation: SliderOrientation,
    zoomHitSlopPx: Float,
    onInteractionStart: (() -> Unit)?,
    onInteractionEnd: (() -> Unit)?,
) {
    awaitEachGesture {
        val down = awaitFirstDown(requireUnconsumed = false)
        val downPosition = down.position
        var zoom = 1f
        var pan = Offset.Zero
        var pastTouchSlop = false
        var gestureMode: ZoomGestureMode? = null
        val touchSlop = viewConfiguration.touchSlop
        var notifiedStart = false

        do {
            val event = awaitPointerEvent()
            val canceled = event.changes.any { it.isConsumed }
            if (canceled) break

            val zoomChange = event.calculateZoom()
            val panChange = event.calculatePan()
            val pointerCount = event.changes.count { it.pressed }

            if (!pastTouchSlop) {
                zoom *= zoomChange
                pan += panChange
                val centroidSize = event.calculateCentroidSize(useCurrent = false)
                val zoomMotion = abs(1 - zoom) * centroidSize
                val panMotion = pan.getDistance()
                if (zoomMotion > touchSlop || panMotion > touchSlop) {
                    pastTouchSlop = true
                }
            }

            if (pastTouchSlop) {
                if (!notifiedStart) {
                    onInteractionStart?.invoke()
                    notifiedStart = true
                }
                val centroid = event.calculateCentroid(useCurrent = false)
                val viewport = sizeProvider()

                if (pointerCount >= 2) {
                    gestureMode = ZoomGestureMode.Pinch
                    zoomState.applyGesture(
                        centroid = centroid,
                        pan = panChange,
                        zoom = zoomChange,
                        viewport = viewport,
                    )
                } else {
                    if (gestureMode == ZoomGestureMode.Pinch) {
                        gestureMode = ZoomGestureMode.Pan
                    }
                    val mode = gestureMode ?: run {
                        val nearDivider = isNearSliderDivider(
                            position = downPosition,
                            progress = state.progress,
                            orientation = orientation,
                            viewport = viewport,
                            hitSlopPx = zoomHitSlopPx,
                        )
                        if (zoomState.isZoomed && !nearDivider) {
                            ZoomGestureMode.Pan
                        } else {
                            ZoomGestureMode.Slider
                        }
                    }
                    gestureMode = mode
                    when (mode) {
                        ZoomGestureMode.Slider -> {
                            val dragDelta = when (orientation) {
                                SliderOrientation.Horizontal -> panChange.x
                                SliderOrientation.Vertical -> panChange.y
                            }
                            applySliderDrag(
                                state = state,
                                sizeProvider = sizeProvider,
                                orientation = orientation,
                                dragDelta = dragDelta,
                            )
                        }

                        ZoomGestureMode.Pan, ZoomGestureMode.Pinch -> {
                            zoomState.applyGesture(
                                centroid = centroid,
                                pan = panChange,
                                zoom = 1f,
                                viewport = viewport,
                            )
                        }
                    }
                }

                event.changes.forEach { change ->
                    if (change.positionChanged()) {
                        change.consume()
                    }
                }
            }
        } while (event.changes.any { it.pressed })

        if (notifiedStart) {
            onInteractionEnd?.invoke()
        }
    }
}

private suspend fun PointerInputScope.detectScrollZoomGestures(
    zoomState: CompareZoomState,
    sizeProvider: () -> IntSize,
) {
    awaitPointerEventScope {
        while (true) {
            val event = awaitPointerEvent()
            if (event.type != PointerEventType.Scroll) continue
            val change = event.changes.firstOrNull() ?: continue
            val scrollY = change.scrollDelta.y
            if (scrollY == 0f) continue
            zoomState.applyGesture(
                centroid = change.position,
                pan = Offset.Zero,
                zoom = zoomFactorFromScrollDelta(scrollY),
                viewport = sizeProvider(),
            )
            change.consume()
        }
    }
}

private fun applySliderDrag(
    state: SliderState,
    sizeProvider: () -> IntSize,
    orientation: SliderOrientation,
    dragDelta: Float,
) {
    val primaryDimension = sliderPrimaryDimension(sizeProvider(), orientation)
    if (primaryDimension <= 0f) return
    state.updateProgress(state.progress + (dragDelta / primaryDimension))
}

private enum class ZoomGestureMode {
    Slider,
    Pan,
    Pinch,
}
