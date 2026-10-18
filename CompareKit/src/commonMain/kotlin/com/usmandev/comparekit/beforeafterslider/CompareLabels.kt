package com.usmandev.comparekit.beforeafterslider

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp

/**
 * Vertical and horizontal placement of before and after labels on [BeforeAfterSlider].
 *
 * For horizontal sliders, before labels anchor to the left image region and after labels
 * anchor to the right image region. For vertical sliders, before labels anchor to the
 * bottom image region and after labels anchor to the top image region. Positions mirror
 * each other so labels stay near their respective image sides.
 */
enum class CompareLabelAlignment {
    /** Before at the top-start; after at the top-end (horizontal) or top-start (vertical). */
    TopStart,

    /** Before at the top-center of the before region; after at the top-center of the after region. */
    TopCenter,

    /** Before at the top-end of the before region; after at the top-start of the after region. */
    TopEnd,

    /** Before at the bottom-start; after at the bottom-end (horizontal) or bottom-start (vertical). */
    BottomStart,

    /** Before at the bottom-center of the before region; after at the bottom-center of the after region. */
    BottomCenter,

    /** Before at the bottom-end of the before region; after at the bottom-start of the after region. */
    BottomEnd,
}

/**
 * A styled text label suitable for before/after comparison sliders.
 *
 * @param text label text to display.
 * @param modifier modifier applied to the label container.
 * @param backgroundColor background drawn behind the text.
 * @param textColor color of the label text.
 * @param contentPadding inner padding around the text.
 * @param cornerRadius corner radius of the label background.
 * @param style typography applied to the label text.
 */
@Composable
fun CompareLabel(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = CompareLabelDefaults.BackgroundColor,
    textColor: Color = CompareLabelDefaults.TextColor,
    contentPadding: PaddingValues = CompareLabelDefaults.ContentPadding,
    cornerRadius: Dp = CompareLabelDefaults.CornerRadius,
    style: TextStyle = CompareLabelDefaults.TextStyle,
) {
    val shape = remember(cornerRadius) { RoundedCornerShape(cornerRadius) }
    Box(
        modifier = modifier
            .background(color = backgroundColor, shape = shape)
            .padding(contentPadding),
    ) {
        BasicText(
            text = text,
            style = style.copy(color = textColor),
        )
    }
}
