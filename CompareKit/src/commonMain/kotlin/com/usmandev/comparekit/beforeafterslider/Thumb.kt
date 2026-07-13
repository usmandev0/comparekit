package com.usmandev.comparekit.beforeafterslider

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp

/**
 * Draws a thumb centered within its layout bounds.
 *
 * When [content] is provided it fully replaces the default thumb rendering.
 * When [painter] is provided the image is clipped to [shape] with an optional border.
 * Otherwise a solid [color] shape is drawn.
 *
 * @param modifier layout modifier applied to the thumb.
 * @param size width and height of the thumb.
 * @param color fill color of the thumb when no [painter] is supplied.
 * @param shape shape of the thumb.
 * @param borderWidth width of the optional border stroke.
 * @param borderColor color of the optional border stroke.
 * @param painter optional icon or image displayed inside the thumb.
 * @param painterPadding inset applied around [painter] inside the thumb.
 * @param content optional custom thumb composable that receives a sized [Modifier].
 */
@Composable
internal fun SliderThumb(
    modifier: Modifier = Modifier,
    size: Dp = CompareKitDefaults.ThumbSize,
    color: Color = CompareKitDefaults.ThumbColor,
    shape: Shape = CompareKitDefaults.ThumbShape,
    borderWidth: Dp = CompareKitDefaults.ThumbBorderWidth,
    borderColor: Color = CompareKitDefaults.ThumbBorderColor,
    painter: Painter? = null,
    painterPadding: Dp = CompareKitDefaults.ThumbIconPadding,
    content: (@Composable (Modifier) -> Unit)? = null,
) {
    when {
        content != null -> content(modifier.size(size))

        painter != null -> {
            val thumbModifier = modifier
                .size(size)
                .clip(shape)
                .background(color)
                .then(
                    if (borderWidth > Dp.Hairline) {
                        Modifier.border(width = borderWidth, color = borderColor, shape = shape)
                    } else {
                        Modifier
                    },
                )
            Box(
                modifier = thumbModifier,
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(painterPadding),
                )
            }
        }

        else -> {
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
    }
}
