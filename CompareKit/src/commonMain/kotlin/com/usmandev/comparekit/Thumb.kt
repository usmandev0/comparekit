package com.usmandev.comparekit

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp

/**
 * Draws a circular thumb centered within its layout bounds.
 *
 * @param modifier layout modifier applied to the thumb canvas.
 * @param size diameter of the thumb.
 * @param color fill color of the thumb.
 * @param strokeWidth width of the optional outline stroke.
 * @param strokeColor color of the optional outline stroke.
 */
@Composable
internal fun SliderThumb(
    modifier: Modifier = Modifier,
    size: Dp = SliderDefaults.ThumbSize,
    color: Color = SliderDefaults.ThumbColor,
    strokeWidth: Dp = SliderDefaults.ThumbStrokeWidth,
    strokeColor: Color = SliderDefaults.ThumbStrokeColor,
) {
    Canvas(modifier = modifier.size(size)) {
        val radius = this.size.minDimension / 2f
        val center = Offset(x = this.size.width / 2f, y = this.size.height / 2f)
        drawCircle(
            color = color,
            radius = radius,
            center = center,
        )
        val strokePx = strokeWidth.toPx()
        if (strokePx > 0f) {
            drawCircle(
                color = strokeColor,
                radius = radius - strokePx / 2f,
                center = center,
                style = Stroke(width = strokePx),
            )
        }
    }
}
