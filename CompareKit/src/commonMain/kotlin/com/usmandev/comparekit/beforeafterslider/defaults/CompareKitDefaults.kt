package com.usmandev.comparekit.beforeafterslider.defaults

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.usmandev.comparekit.beforeafterslider.CompareAnimationMode

/**
 * Default visual and behavioral values for
 * [com.usmandev.comparekit.beforeafterslider.BeforeAfterSlider].
 */
object CompareKitDefaults {
    /** Default slider position as a fraction of the slider axis (`0f`–`1f`). */
    const val SliderPosition: Float = LayoutDefaults.SliderPosition

    /** Alpha multiplier applied to the divider and thumb when the slider is disabled. */
    const val DisabledAlpha: Float = LayoutDefaults.DisabledAlpha

    /** Color of the divider line. */
    val DividerColor: Color = DividerDefaults.FillColor

    /** Stroke thickness of the divider line. */
    val DividerThickness: Dp = DividerDefaults.Thickness

    /** Fill color of the drag thumb. */
    val ThumbColor: Color = ThumbDefaults.FillColor

    /** Diameter of the drag thumb. */
    val ThumbSize: Dp = ThumbDefaults.Size

    /** Shape of the drag thumb. */
    val ThumbShape: Shape = ThumbDefaults.DefaultShape

    /** Border color drawn around the thumb for contrast on light images. */
    val ThumbBorderColor: Color = ThumbDefaults.BorderColor

    /** Border width drawn around the thumb. */
    val ThumbBorderWidth: Dp = ThumbDefaults.BorderWidth

    /** Padding applied around a thumb icon or image. */
    val ThumbIconPadding: Dp = ThumbDefaults.IconPadding

    /** Corner radius used to clip the slider and its images. */
    val CornerRadius: Dp = LayoutDefaults.CornerRadius

    /** Background color behind the before and after images. */
    val BackgroundColor: Color = LayoutDefaults.BackgroundColor

    /** Default auto-scrub animation mode. */
    val AutoScrubAnimationMode: CompareAnimationMode = AnimationDefaults.AutoScrubMode

    /** Duration of one auto-scrub leg (0→1 or 1→0) in milliseconds. */
    const val AutoScrubDurationMillis: Int = AnimationDefaults.AutoScrubDurationMillis

    /** When `true`, auto-scrub pauses while the user is dragging the slider. */
    const val PauseAutoScrubOnInteraction: Boolean = AnimationDefaults.PauseAutoScrubOnInteraction

    /** Default accessibility content description for the slider. */
    const val ContentDescription: String = AccessibilityDefaults.ContentDescription

    /** Default progress step for keyboard and accessibility adjust actions. */
    const val ProgressStep: Float = AccessibilityDefaults.ProgressStep
}
