package com.usmandev.comparekit.beforeafterslider

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Default visual values for [CompareLabel] and label placement on [BeforeAfterSlider].
 */
object CompareLabelDefaults {
    /** Default text shown on the before label when no custom label is provided. */
    const val BeforeText: String = "Before"

    /** Default text shown on the after label when no custom label is provided. */
    const val AfterText: String = "After"

    /** Inset applied between labels and the edges of the slider. */
    val Padding: Dp = 8.dp

    /** Additional offset applied to both labels relative to their aligned position. */
    val Offset: DpOffset = DpOffset.Zero

    /** Semi-transparent background drawn behind default label text. */
    val BackgroundColor: Color = Color.Black.copy(alpha = 0.6f)

    /** Text color used by default labels. */
    val TextColor: Color = Color.White

    /** Corner radius of the default label background. */
    val CornerRadius: Dp = 4.dp

    /** Inner padding around default label text. */
    val ContentPadding: PaddingValues = PaddingValues(horizontal = 8.dp, vertical = 4.dp)

    /**
     * Typography for default labels, approximating Material 3 `labelSmall`.
     */
    val TextStyle: TextStyle = TextStyle(
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    )
}
