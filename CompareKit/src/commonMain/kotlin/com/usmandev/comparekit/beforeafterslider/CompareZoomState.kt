package com.usmandev.comparekit.beforeafterslider

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import com.usmandev.comparekit.beforeafterslider.internal.clampZoomOffset
import com.usmandev.comparekit.beforeafterslider.internal.clampZoomScale
import com.usmandev.comparekit.beforeafterslider.internal.zoomOffsetAfterTransform
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

/**
 * Shared zoom and pan for a [BeforeAfterSlider].
 *
 * Both images use the same [scale] and offset so they stay pixel-aligned while
 * the divider continues to clip in viewport space.
 *
 * @param minScale lower zoom bound; values below `1` are coerced to `1`.
 * @param maxScale upper zoom bound; must be at least [minScale].
 * @param initialScale starting zoom; clamped to `[`[minScale]`,`[maxScale]`]`.
 */
@Stable
class CompareZoomState(
    minScale: Float = CompareKitDefaults.MinZoomScale,
    maxScale: Float = CompareKitDefaults.MaxZoomScale,
    initialScale: Float = CompareKitDefaults.MinZoomScale,
) {
    val minScale: Float = minScale.coerceAtLeast(1f)
    val maxScale: Float = maxScale.coerceAtLeast(this.minScale)

    /**
     * Current zoom factor. `1` shows the image at layout size.
     */
    var scale: Float by mutableFloatStateOf(
        clampZoomScale(initialScale, this.minScale, this.maxScale),
    )
        private set

    /**
     * Horizontal translation in pixels after scaling around the top-start origin.
     */
    var offsetX: Float by mutableFloatStateOf(0f)
        private set

    /**
     * Vertical translation in pixels after scaling around the top-start origin.
     */
    var offsetY: Float by mutableFloatStateOf(0f)
        private set

    /** Current pan offset. */
    val offset: Offset
        get() = Offset(offsetX, offsetY)

    /** `true` when the images are magnified past [minScale]. */
    val isZoomed: Boolean
        get() = scale > minScale + ZoomedEpsilon

    private var gestureGeneration: Int = 0

    /**
     * Applies a pinch or pan gesture around [centroid] in viewport coordinates.
     */
    fun applyGesture(
        centroid: Offset,
        pan: Offset,
        zoom: Float,
        viewport: IntSize,
    ) {
        if (viewport.width <= 0 || viewport.height <= 0) return
        if (!centroid.x.isFinite() || !centroid.y.isFinite()) return
        if (!pan.x.isFinite() || !pan.y.isFinite() || !zoom.isFinite() || zoom <= 0f) return
        gestureGeneration += 1
        val currentScale = scale
        val newScale = clampZoomScale(currentScale * zoom, minScale, maxScale)
        val nextOffset = zoomOffsetAfterTransform(
            offsetX = offsetX,
            offsetY = offsetY,
            currentScale = currentScale,
            newScale = newScale,
            centroid = centroid,
            pan = pan,
        )
        val clamped = clampZoomOffset(
            offsetX = nextOffset.x,
            offsetY = nextOffset.y,
            scale = newScale,
            viewport = viewport,
        )
        scale = newScale
        offsetX = clamped.x
        offsetY = clamped.y
    }

    /**
     * Re-clamps the current offset after a layout size change.
     */
    fun coerceToViewport(viewport: IntSize) {
        val clamped = clampZoomOffset(offsetX, offsetY, scale, viewport)
        offsetX = clamped.x
        offsetY = clamped.y
    }

    /**
     * Snaps zoom and pan back to the unzoomed identity transform.
     */
    fun reset() {
        gestureGeneration += 1
        scale = minScale
        offsetX = 0f
        offsetY = 0f
    }

    /**
     * Animates zoom and pan back to the unzoomed identity transform.
     */
    suspend fun animateReset() {
        val token = ++gestureGeneration
        val scaleAnim = Animatable(scale)
        val xAnim = Animatable(offsetX)
        val yAnim = Animatable(offsetY)
        val spec = spring<Float>(stiffness = Spring.StiffnessMediumLow)
        coroutineScope {
            launch {
                scaleAnim.animateTo(minScale, spec) {
                    if (token == gestureGeneration) scale = value
                }
            }
            launch {
                xAnim.animateTo(0f, spec) {
                    if (token == gestureGeneration) offsetX = value
                }
            }
            launch {
                yAnim.animateTo(0f, spec) {
                    if (token == gestureGeneration) offsetY = value
                }
            }
        }
    }

    private companion object {
        const val ZoomedEpsilon: Float = 0.001f
    }
}

/**
 * Creates and remembers a [CompareZoomState].
 *
 * @param minScale lower zoom bound; defaults to `1`.
 * @param maxScale upper zoom bound; defaults to `4`.
 * @param initialScale starting zoom; defaults to [minScale].
 */
@Composable
fun rememberCompareZoomState(
    minScale: Float = CompareKitDefaults.MinZoomScale,
    maxScale: Float = CompareKitDefaults.MaxZoomScale,
    initialScale: Float = minScale,
): CompareZoomState {
    return remember(minScale, maxScale) {
        CompareZoomState(
            minScale = minScale,
            maxScale = maxScale,
            initialScale = initialScale,
        )
    }
}
