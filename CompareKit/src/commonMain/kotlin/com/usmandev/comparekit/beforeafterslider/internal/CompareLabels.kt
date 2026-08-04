package com.usmandev.comparekit.beforeafterslider.internal

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import com.usmandev.comparekit.beforeafterslider.CompareLabelAlignment
import com.usmandev.comparekit.beforeafterslider.SliderOrientation

/**
 * Overlays before and after labels above slider content without intercepting drag gestures.
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
