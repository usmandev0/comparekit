package com.usmandev.comparekit.beforeafterslider

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Default visual and behavioral values for [BeforeAfterSlider].
 */
object CompareKitDefaults {
    /** Default slider position as a fraction of the slider axis (`0f`–`1f`). */
    const val SliderPosition: Float = 0.5f

    /** Alpha multiplier applied to the divider and thumb when [BeforeAfterSlider] is disabled. */
    const val DisabledAlpha: Float = 0.38f

    /** Color of the divider line. */
    val DividerColor: Color = Color.White

    /** Stroke thickness of the divider line. */
    val DividerThickness: Dp = 2.dp

    /** Fill color of the drag thumb. */
    val ThumbColor: Color = Color.White

    /** Diameter of the drag thumb. */
    val ThumbSize: Dp = 32.dp

    /** Shape of the drag thumb. */
    val ThumbShape: Shape = CircleShape

    /** Border color drawn around the thumb for contrast on light images. */
    val ThumbBorderColor: Color = Color.Black.copy(alpha = 0.12f)

    /** Border width drawn around the thumb. */
    val ThumbBorderWidth: Dp = 1.dp

    /** Padding applied around a [BeforeAfterSlider] thumb icon or image. */
    val ThumbIconPadding: Dp = 4.dp

    /** Corner radius used to clip the slider and its images. */
    val CornerRadius: Dp = 0.dp

    /** Background color behind the before and after images. */
    val BackgroundColor: Color = Color.Transparent

    /** Default auto-scrub animation mode for [BeforeAfterSlider]. */
    val AutoScrubAnimationMode: CompareAnimationMode = CompareAnimationMode.None

    /** Duration of one auto-scrub leg (0→1 or 1→0) in milliseconds. */
    const val AutoScrubDurationMillis: Int = 2000

    /** When `true`, auto-scrub pauses while the user is dragging the slider. */
    const val PauseAutoScrubOnInteraction: Boolean = true
}
