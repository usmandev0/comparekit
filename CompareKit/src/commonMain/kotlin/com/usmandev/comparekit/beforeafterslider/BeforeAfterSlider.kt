package com.usmandev.comparekit.beforeafterslider

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import kotlin.math.roundToInt

/**
 * A before/after image comparison slider built with Compose Multiplatform.
 *
 * The [before] image is clipped to the right of the divider and the [after]
 * image is clipped to the left, so their backgrounds never overlap. A vertical
 * divider and thumb follow the drag position.
 *
 * @param before painter for the fully visible "before" image.
 * @param after painter for the "after" image, clipped by slider progress.
 * @param modifier modifier applied to the root container.
 * @param contentScale scaling applied to both images.
 * @param sliderPosition initial horizontal position as a fraction of width
 * (`0f`–`1f`); used when [state] is not supplied explicitly.
 * @param state slider progress state; defaults to [sliderPosition] via
 * [rememberCompareSliderState].
 * @param dividerColor color of the vertical divider line.
 * @param dividerThickness width of the vertical divider line.
 * @param thumbColor fill color of the drag thumb.
 * @param thumbSize width and height of the drag thumb.
 * @param thumbShape shape of the drag thumb.
 * @param thumbBorderColor border color drawn around the thumb.
 * @param thumbBorderWidth border width drawn around the thumb.
 * @param cornerRadius corner radius used to clip the slider and both images.
 * @param backgroundColor background color behind the images.
 * @param showDivider when `false`, the divider line is not drawn.
 * @param showThumb when `false`, the thumb is not drawn.
 * @param enabled when `false`, all gestures are ignored and the divider and thumb
 * are drawn with reduced alpha.
 * @param doubleTapToReset when `true`, a double tap animates the slider back to
 * [defaultSliderPosition].
 * @param defaultSliderPosition target progress used when resetting via double tap.
 * @param onDoubleTap optional callback invoked when a double tap is detected.
 * @param onLongPress optional callback invoked when a long press is detected.
 * @param beforeLabel optional composable for the before label; defaults to
 * [CompareLabelDefaults.BeforeText] when `null`.
 * @param afterLabel optional composable for the after label; defaults to
 * [CompareLabelDefaults.AfterText] when `null`.
 * @param showBeforeLabel when `false`, the before label is not drawn.
 * @param showAfterLabel when `false`, the after label is not drawn.
 * @param labelAlignment placement of labels within their respective image regions.
 * @param labelPadding inset between labels and the slider edges.
 * @param labelOffset additional offset applied to both labels.
 */
@Composable
fun BeforeAfterSlider(
    before: Painter,
    after: Painter,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    sliderPosition: Float = CompareKitDefaults.SliderPosition,
    state: SliderState = rememberCompareSliderState(initialProgress = sliderPosition),
    dividerColor: Color = CompareKitDefaults.DividerColor,
    dividerThickness: Dp = CompareKitDefaults.DividerThickness,
    thumbColor: Color = CompareKitDefaults.ThumbColor,
    thumbSize: Dp = CompareKitDefaults.ThumbSize,
    thumbShape: Shape = CompareKitDefaults.ThumbShape,
    thumbBorderColor: Color = CompareKitDefaults.ThumbBorderColor,
    thumbBorderWidth: Dp = CompareKitDefaults.ThumbBorderWidth,
    cornerRadius: Dp = CompareKitDefaults.CornerRadius,
    backgroundColor: Color = CompareKitDefaults.BackgroundColor,
    showDivider: Boolean = true,
    showThumb: Boolean = true,
    enabled: Boolean = true,
    doubleTapToReset: Boolean = true,
    defaultSliderPosition: Float = CompareKitDefaults.SliderPosition,
    onDoubleTap: (() -> Unit)? = null,
    onLongPress: (() -> Unit)? = null,
    beforeLabel: (@Composable () -> Unit)? = null,
    afterLabel: (@Composable () -> Unit)? = null,
    showBeforeLabel: Boolean = true,
    showAfterLabel: Boolean = true,
    labelAlignment: CompareLabelAlignment = CompareLabelAlignment.TopStart,
    labelPadding: Dp = CompareLabelDefaults.Padding,
    labelOffset: DpOffset = CompareLabelDefaults.Offset,
) {
    var layoutSize by remember { mutableStateOf(IntSize.Zero) }
    val density = LocalDensity.current
    val thumbSizePx = remember(density, thumbSize) { with(density) { thumbSize.toPx() } }
    val dividerThicknessPx = remember(density, dividerThickness) {
        with(density) { dividerThickness.toPx() }
    }
    val clipShape = remember(cornerRadius, density) {
        RoundedCornerShape(with(density) { cornerRadius.toPx() })
    }
    val effectiveDividerColor = remember(dividerColor, enabled) {
        if (enabled) {
            dividerColor
        } else {
            dividerColor.copy(alpha = dividerColor.alpha * CompareKitDefaults.DisabledAlpha)
        }
    }
    val effectiveThumbColor = remember(thumbColor, enabled) {
        if (enabled) {
            thumbColor
        } else {
            thumbColor.copy(alpha = thumbColor.alpha * CompareKitDefaults.DisabledAlpha)
        }
    }
    val effectiveThumbBorderColor = remember(thumbBorderColor, enabled) {
        if (enabled) {
            thumbBorderColor
        } else {
            thumbBorderColor.copy(alpha = thumbBorderColor.alpha * CompareKitDefaults.DisabledAlpha)
        }
    }
    val currentOnDoubleTap = rememberUpdatedState(onDoubleTap)
    val currentOnLongPress = rememberUpdatedState(onLongPress)
    val handleDoubleTap = remember {
        {
            currentOnDoubleTap.value?.invoke()
            Unit
        }
    }
    val handleLongPress = remember {
        {
            currentOnLongPress.value?.invoke()
            Unit
        }
    }

    Box(
        modifier = modifier
            .clip(clipShape)
            .background(backgroundColor)
            .onSizeChanged { layoutSize = it }
            .sliderGestures(
                state = state,
                sizeProvider = { layoutSize },
                enabled = enabled,
                doubleTapToReset = doubleTapToReset,
                defaultSliderPosition = defaultSliderPosition,
                onDoubleTap = handleDoubleTap,
                onLongPress = handleLongPress,
            ),
    ) {
        Image(
            painter = before,
            contentDescription = null,
            contentScale = contentScale,
            modifier = Modifier
                .fillMaxSize()
                .drawWithContent {
                    val clipStart = size.width * state.progress
                    clipRect(
                        left = clipStart,
                        top = 0f,
                        right = size.width,
                        bottom = size.height,
                    ) {
                        this@drawWithContent.drawContent()
                    }
                },
        )

        Image(
            painter = after,
            contentDescription = null,
            contentScale = contentScale,
            modifier = Modifier
                .fillMaxSize()
                .drawWithContent {
                    val clipEnd = size.width * state.progress
                    clipRect(
                        left = 0f,
                        top = 0f,
                        right = clipEnd,
                        bottom = size.height,
                    ) {
                        this@drawWithContent.drawContent()
                    }
                },
        )

        if (showDivider) {
            val dividerOffsetX = ((layoutSize.width * state.progress) - (dividerThicknessPx / 2f))
                .roundToInt()
            SliderDivider(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .offset { IntOffset(x = dividerOffsetX, y = 0) },
                thickness = dividerThickness,
                color = effectiveDividerColor,
            )
        }

        if (showThumb) {
            val thumbOffsetX = ((layoutSize.width * state.progress) - (thumbSizePx / 2f))
                .roundToInt()
            SliderThumb(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .offset { IntOffset(x = thumbOffsetX, y = 0) },
                size = thumbSize,
                color = effectiveThumbColor,
                shape = thumbShape,
                borderWidth = thumbBorderWidth,
                borderColor = effectiveThumbBorderColor,
            )
        }

        CompareSliderLabels(
            showBeforeLabel = showBeforeLabel,
            showAfterLabel = showAfterLabel,
            alignment = labelAlignment,
            padding = labelPadding,
            offset = labelOffset,
            beforeLabel = {
                if (beforeLabel != null) {
                    beforeLabel()
                } else {
                    CompareLabel(text = CompareLabelDefaults.BeforeText)
                }
            },
            afterLabel = {
                if (afterLabel != null) {
                    afterLabel()
                } else {
                    CompareLabel(text = CompareLabelDefaults.AfterText)
                }
            },
        )
    }
}
