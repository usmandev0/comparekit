package com.usmandev.comparekit.beforeafterslider.defaults

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

internal object ThumbDefaults {
    val FillColor: Color = Color.White
    val Size: Dp = 32.dp
    val DefaultShape: Shape = CircleShape
    val BorderColor: Color = Color.Black.copy(alpha = 0.12f)
    val BorderWidth: Dp = 1.dp
    val IconPadding: Dp = 4.dp
}
