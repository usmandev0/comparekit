package com.usmandev.comparekit.beforeafterslider

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

/**
 * Draws a vertical divider line that spans the full height of its parent.
 *
 * @param modifier layout modifier applied to the divider canvas.
 * @param thickness width of the divider line.
 * @param color color of the divider line.
 */
@Composable
internal fun SliderDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = CompareKitDefaults.DividerThickness,
    color: Color = CompareKitDefaults.DividerColor,
) {
    Canvas(
        modifier = modifier
            .width(thickness)
            .fillMaxHeight(),
    ) {
        val strokeWidth = size.width
        val x = size.width / 2f
        drawLine(
            color = color,
            start = Offset(x = x, y = 0f),
            end = Offset(x = x, y = size.height),
            strokeWidth = strokeWidth,
        )
    }
}
