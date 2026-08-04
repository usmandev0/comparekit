package com.usmandev.comparekit.beforeafterslider.internal

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.usmandev.comparekit.beforeafterslider.CompareKitDefaults
import com.usmandev.comparekit.beforeafterslider.SliderOrientation

/**
 * Draws the divider line that spans the full cross-axis of its parent.
 */
@Composable
internal fun SliderDivider(
    modifier: Modifier = Modifier,
    orientation: SliderOrientation = SliderOrientation.Horizontal,
    thickness: Dp = CompareKitDefaults.DividerThickness,
    color: Color = CompareKitDefaults.DividerColor,
) {
    when (orientation) {
        SliderOrientation.Horizontal -> {
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

        SliderOrientation.Vertical -> {
            Canvas(
                modifier = modifier
                    .height(thickness)
                    .fillMaxWidth(),
            ) {
                val strokeWidth = size.height
                val y = size.height / 2f
                drawLine(
                    color = color,
                    start = Offset(x = 0f, y = y),
                    end = Offset(x = size.width, y = y),
                    strokeWidth = strokeWidth,
                )
            }
        }
    }
}
