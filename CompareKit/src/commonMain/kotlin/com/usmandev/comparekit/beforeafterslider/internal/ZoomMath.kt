package com.usmandev.comparekit.beforeafterslider.internal

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import com.usmandev.comparekit.beforeafterslider.SliderOrientation
import kotlin.math.abs

internal fun clampZoomScale(
    scale: Float,
    minScale: Float,
    maxScale: Float,
): Float {
    val min = minScale.coerceAtLeast(1f)
    val max = maxScale.coerceAtLeast(min)
    if (!scale.isFinite()) return min
    return scale.coerceIn(min, max)
}

/**
 * Keeps a top-start transform origin zoom covering the viewport with no empty edges.
 *
 * At [scale] `1`, the only valid offset is `0`. At [scale] `> 1`, offset is clamped so
 * the scaled image always fills `[0, viewport]`.
 */
internal fun clampZoomOffset(
    offsetX: Float,
    offsetY: Float,
    scale: Float,
    viewport: IntSize,
): Offset {
    if (viewport.width <= 0 || viewport.height <= 0 || !scale.isFinite()) {
        return Offset.Zero
    }
    val minX = viewport.width * (1f - scale)
    val minY = viewport.height * (1f - scale)
    val x = if (offsetX.isFinite()) offsetX.coerceIn(minX, 0f) else 0f
    val y = if (offsetY.isFinite()) offsetY.coerceIn(minY, 0f) else 0f
    return Offset(x = x, y = y)
}

/**
 * Offset after scaling around [centroid] then applying [pan], using a top-start
 * transform origin: `screen = content * scale + offset`.
 */
internal fun zoomOffsetAfterTransform(
    offsetX: Float,
    offsetY: Float,
    currentScale: Float,
    newScale: Float,
    centroid: Offset,
    pan: Offset,
): Offset {
    val factor = if (currentScale == 0f) 1f else newScale / currentScale
    return Offset(
        x = centroid.x - (centroid.x - offsetX) * factor + pan.x,
        y = centroid.y - (centroid.y - offsetY) * factor + pan.y,
    )
}

internal fun isNearSliderDivider(
    position: Offset,
    progress: Float,
    orientation: SliderOrientation,
    viewport: IntSize,
    hitSlopPx: Float,
): Boolean {
    if (viewport.width <= 0 || viewport.height <= 0) return false
    val dividerPosition = when (orientation) {
        SliderOrientation.Horizontal -> viewport.width * progress
        SliderOrientation.Vertical -> viewport.height * progress
    }
    val touch = when (orientation) {
        SliderOrientation.Horizontal -> position.x
        SliderOrientation.Vertical -> position.y
    }
    return abs(touch - dividerPosition) <= hitSlopPx
}

internal fun zoomFactorFromScrollDelta(scrollY: Float): Float {
    return (1f - scrollY * 0.004f).coerceIn(0.5f, 1.5f)
}
