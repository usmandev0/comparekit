package com.usmandev.comparekit.beforeafterslider

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp

/**
 * Draws a thumb centered within its layout bounds.
 *
 * @param modifier layout modifier applied to the thumb canvas.
 * @param size width and height of the thumb.
 * @param color fill color of the thumb.
 * @param shape shape of the thumb.
 * @param borderWidth width of the optional border stroke.
 * @param borderColor color of the optional border stroke.
 */
@Composable
internal fun SliderThumb(
    modifier: Modifier = Modifier,
    size: Dp = CompareKitDefaults.ThumbSize,
    color: Color = CompareKitDefaults.ThumbColor,
    shape: Shape = CompareKitDefaults.ThumbShape,
    borderWidth: Dp = CompareKitDefaults.ThumbBorderWidth,
    borderColor: Color = CompareKitDefaults.ThumbBorderColor,
) {
    val layoutDirection = LocalLayoutDirection.current
    Canvas(modifier = modifier.size(size)) {
        val outline = shape.createOutline(this.size, layoutDirection, this)
        drawOutline(outline = outline, color = color)
        val borderPx = borderWidth.toPx()
        if (borderPx > 0f) {
            drawOutline(
                outline = outline,
                color = borderColor,
                style = Stroke(width = borderPx),
            )
        }
    }
}
