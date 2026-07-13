package com.usmandev.comparekit.beforeafterslider

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset

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

/**
 * Overlays before and after labels above slider content without intercepting drag gestures.
 *
 * @param showBeforeLabel when `false`, the before label is not drawn.
 * @param showAfterLabel when `false`, the after label is not drawn.
 * @param alignment shared alignment used to position both labels within their image regions.
 * @param orientation slider orientation used to split label regions.
 * @param padding inset between labels and the slider edges.
 * @param offset additional offset applied to the label overlay.
 * @param beforeLabel composable content for the before label.
 * @param afterLabel composable content for the after label.
 * @param modifier modifier applied to the overlay container.
 */
@Composable
internal fun CompareSliderLabels(
    showBeforeLabel: Boolean,
    showAfterLabel: Boolean,
    alignment: CompareLabelAlignment,
    orientation: SliderOrientation,
    padding: Dp,
    offset: DpOffset,
    beforeLabel: @Composable () -> Unit,
    afterLabel: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (!showBeforeLabel && !showAfterLabel) return

    Box(
        modifier = modifier
            .fillMaxSize()
            .compareLabelPointerPassthrough()
            .padding(padding)
            .offset(x = offset.x, y = offset.y),
    ) {
        when (orientation) {
            SliderOrientation.Horizontal -> {
                if (showBeforeLabel) {
                    CompareHorizontalLabelRegion(
                        modifier = Modifier.align(Alignment.CenterStart),
                        horizontalAlignment = alignment.beforeHorizontalAlignment(),
                        verticalAlignment = alignment.verticalAlignment(),
                        content = beforeLabel,
                    )
                }
                if (showAfterLabel) {
                    CompareHorizontalLabelRegion(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        horizontalAlignment = alignment.afterHorizontalAlignment(),
                        verticalAlignment = alignment.verticalAlignment(),
                        content = afterLabel,
                    )
                }
            }

            SliderOrientation.Vertical -> {
                if (showAfterLabel) {
                    CompareVerticalLabelRegion(
                        modifier = Modifier.align(Alignment.TopCenter),
                        horizontalAlignment = alignment.afterVerticalHorizontalAlignment(),
                        verticalAlignment = alignment.afterVerticalAlignment(),
                        content = afterLabel,
                    )
                }
                if (showBeforeLabel) {
                    CompareVerticalLabelRegion(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        horizontalAlignment = alignment.beforeVerticalHorizontalAlignment(),
                        verticalAlignment = alignment.beforeVerticalAlignment(),
                        content = beforeLabel,
                    )
                }
            }
        }
    }
}

@Composable
private fun CompareHorizontalLabelRegion(
    horizontalAlignment: Alignment.Horizontal,
    verticalAlignment: Alignment.Vertical,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth(CompareLabelLayout.HalfFraction),
    ) {
        Box(
            modifier = Modifier.align(
                toBoxAlignment(
                    horizontal = horizontalAlignment,
                    vertical = verticalAlignment,
                ),
            ),
        ) {
            content()
        }
    }
}

@Composable
private fun CompareVerticalLabelRegion(
    horizontalAlignment: Alignment.Horizontal,
    verticalAlignment: Alignment.Vertical,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(CompareLabelLayout.HalfFraction),
    ) {
        Box(
            modifier = Modifier.align(
                toBoxAlignment(
                    horizontal = horizontalAlignment,
                    vertical = verticalAlignment,
                ),
            ),
        ) {
            content()
        }
    }
}

private fun toBoxAlignment(
    horizontal: Alignment.Horizontal,
    vertical: Alignment.Vertical,
): Alignment = when (vertical) {
    Alignment.Top -> when (horizontal) {
        Alignment.Start -> Alignment.TopStart
        Alignment.CenterHorizontally -> Alignment.TopCenter
        Alignment.End -> Alignment.TopEnd
        else -> Alignment.TopStart
    }

    Alignment.Bottom -> when (horizontal) {
        Alignment.Start -> Alignment.BottomStart
        Alignment.CenterHorizontally -> Alignment.BottomCenter
        Alignment.End -> Alignment.BottomEnd
        else -> Alignment.BottomStart
    }

    else -> Alignment.TopStart
}

private fun CompareLabelAlignment.verticalAlignment(): Alignment.Vertical = when (this) {
    CompareLabelAlignment.TopStart,
    CompareLabelAlignment.TopCenter,
    CompareLabelAlignment.TopEnd,
    -> Alignment.Top

    CompareLabelAlignment.BottomStart,
    CompareLabelAlignment.BottomCenter,
    CompareLabelAlignment.BottomEnd,
    -> Alignment.Bottom
}

private fun CompareLabelAlignment.beforeHorizontalAlignment(): Alignment.Horizontal = when (this) {
    CompareLabelAlignment.TopStart,
    CompareLabelAlignment.BottomStart,
    -> Alignment.Start

    CompareLabelAlignment.TopCenter,
    CompareLabelAlignment.BottomCenter,
    -> Alignment.CenterHorizontally

    CompareLabelAlignment.TopEnd,
    CompareLabelAlignment.BottomEnd,
    -> Alignment.End
}

private fun CompareLabelAlignment.afterHorizontalAlignment(): Alignment.Horizontal = when (this) {
    CompareLabelAlignment.TopStart,
    CompareLabelAlignment.BottomStart,
    -> Alignment.End

    CompareLabelAlignment.TopCenter,
    CompareLabelAlignment.BottomCenter,
    -> Alignment.CenterHorizontally

    CompareLabelAlignment.TopEnd,
    CompareLabelAlignment.BottomEnd,
    -> Alignment.Start
}

private fun CompareLabelAlignment.afterVerticalAlignment(): Alignment.Vertical = when (this) {
    CompareLabelAlignment.TopStart,
    CompareLabelAlignment.TopCenter,
    CompareLabelAlignment.TopEnd,
    -> Alignment.Top

    CompareLabelAlignment.BottomStart,
    CompareLabelAlignment.BottomCenter,
    CompareLabelAlignment.BottomEnd,
    -> Alignment.Bottom
}

private fun CompareLabelAlignment.beforeVerticalAlignment(): Alignment.Vertical = when (this) {
    CompareLabelAlignment.TopStart,
    CompareLabelAlignment.TopCenter,
    CompareLabelAlignment.TopEnd,
    -> Alignment.Bottom

    CompareLabelAlignment.BottomStart,
    CompareLabelAlignment.BottomCenter,
    CompareLabelAlignment.BottomEnd,
    -> Alignment.Top
}

private fun CompareLabelAlignment.afterVerticalHorizontalAlignment(): Alignment.Horizontal = when (this) {
    CompareLabelAlignment.TopStart,
    CompareLabelAlignment.BottomStart,
    -> Alignment.Start

    CompareLabelAlignment.TopCenter,
    CompareLabelAlignment.BottomCenter,
    -> Alignment.CenterHorizontally

    CompareLabelAlignment.TopEnd,
    CompareLabelAlignment.BottomEnd,
    -> Alignment.End
}

private fun CompareLabelAlignment.beforeVerticalHorizontalAlignment(): Alignment.Horizontal = when (this) {
    CompareLabelAlignment.TopStart,
    CompareLabelAlignment.BottomStart,
    -> Alignment.Start

    CompareLabelAlignment.TopCenter,
    CompareLabelAlignment.BottomCenter,
    -> Alignment.CenterHorizontally

    CompareLabelAlignment.TopEnd,
    CompareLabelAlignment.BottomEnd,
    -> Alignment.End
}

/**
 * Forwards pointer events to underlying slider content so labels do not block dragging.
 */
private fun Modifier.compareLabelPointerPassthrough(): Modifier = pointerInput(Unit) {
    awaitPointerEventScope {
        while (true) {
            awaitPointerEvent(pass = PointerEventPass.Initial)
        }
    }
}

private object CompareLabelLayout {
    const val HalfFraction: Float = 0.5f
}
