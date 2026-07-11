package com.usmandev.comparekit.beforeafterslider

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

/**
 * Default visual values for [BeforeAfterSlider] and its overlay controls.
 *
 * @deprecated Use [CompareKitDefaults] instead.
 */
@Deprecated(
    message = "Use CompareKitDefaults instead.",
    replaceWith = ReplaceWith("CompareKitDefaults"),
)
object SliderDefaults {
    /** Thickness of the vertical divider line. */
    val DividerThickness: Dp
        get() = CompareKitDefaults.DividerThickness

    /** Diameter of the circular drag thumb. */
    val ThumbSize: Dp
        get() = CompareKitDefaults.ThumbSize

    /** Color of the divider line. */
    val DividerColor: Color
        get() = CompareKitDefaults.DividerColor

    /** Fill color of the circular thumb. */
    val ThumbColor: Color
        get() = CompareKitDefaults.ThumbColor

    /** Stroke width drawn around the thumb for contrast on light images. */
    val ThumbStrokeWidth: Dp
        get() = CompareKitDefaults.ThumbBorderWidth

    /** Stroke color drawn around the thumb. */
    val ThumbStrokeColor: Color
        get() = CompareKitDefaults.ThumbBorderColor
}
