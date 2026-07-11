package com.usmandev.comparekit.beforeafterslider

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Default visual values for [BeforeAfterSlider] and its overlay controls.
 */
object SliderDefaults {
    /** Thickness of the vertical divider line. */
    val DividerThickness: Dp = 2.dp

    /** Diameter of the circular drag thumb. */
    val ThumbSize: Dp = 32.dp

    /** Color of the divider line. */
    val DividerColor: Color = Color.White

    /** Fill color of the circular thumb. */
    val ThumbColor: Color = Color.White

    /** Stroke width drawn around the thumb for contrast on light images. */
    val ThumbStrokeWidth: Dp = 1.dp

    /** Stroke color drawn around the thumb. */
    val ThumbStrokeColor: Color = Color.Black.copy(alpha = 0.12f)
}
