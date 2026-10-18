package com.usmandev.comparekit.beforeafterslider

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.usmandev.comparekit.beforeafterslider.defaults.ZoomDefaults
import kotlin.math.max
import com.usmandev.comparekit.beforeafterslider.internal.CompareSliderLabels
import com.usmandev.comparekit.beforeafterslider.internal.SliderAutoScrubEffect
import com.usmandev.comparekit.beforeafterslider.internal.SliderDivider
import com.usmandev.comparekit.beforeafterslider.internal.SliderThumb
import com.usmandev.comparekit.beforeafterslider.internal.compareSliderKeyboard
import com.usmandev.comparekit.beforeafterslider.internal.compareSliderSemantics
import com.usmandev.comparekit.beforeafterslider.internal.resolveCompareSliderContentDescription
import com.usmandev.comparekit.beforeafterslider.internal.sliderClipBounds
import com.usmandev.comparekit.beforeafterslider.internal.sliderDividerAlignment
import com.usmandev.comparekit.beforeafterslider.internal.sliderDividerOffset
import com.usmandev.comparekit.beforeafterslider.internal.sliderGestures
import com.usmandev.comparekit.beforeafterslider.internal.sliderThumbOffset

/**
 * A before/after image comparison slider built with Compose Multiplatform.
 *
 * The [before] image is clipped to the end side of the divider and the [after]
 * image is clipped to the start side, so their backgrounds never overlap. A divider
 * and thumb follow the drag position along the slider axis.
 *
 * @param before painter for the fully visible "before" image.
 * @param after painter for the "after" image, clipped by slider progress.
 * @param modifier modifier applied to the root container.
 * @param orientation horizontal or vertical comparison axis.
 * @param contentScale scaling applied to both images.
 * @param sliderPosition initial position as a fraction of the slider axis
 * (`0f`–`1f`); used when [state] is not supplied explicitly.
 * @param state slider progress state; defaults to [sliderPosition] via
 * [rememberCompareSliderState].
 * @param dividerColor color of the divider line.
 * @param dividerThickness stroke thickness of the divider line.
 * @param thumbColor fill color of the drag thumb.
 * @param thumbSize width and height of the drag thumb.
 * @param thumbShape shape of the drag thumb.
 * @param thumbBorderColor border color drawn around the thumb.
 * @param thumbBorderWidth border width drawn around the thumb.
 * @param thumbPainter optional icon or image displayed inside the thumb.
 * @param thumbIconPadding inset applied around [thumbPainter] inside the thumb.
 * @param thumbContent optional custom thumb composable that receives a sized [Modifier].
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
 * @param animationMode automatic animation mode for the slider progress.
 * @param autoScrubDurationMillis duration of one auto-scrub leg in milliseconds.
 * @param pauseAutoScrubOnInteraction when `true`, auto-scrub pauses while the user drags.
 * @param contentDescription accessibility label for the slider as a whole.
 * @param beforeContentDescription optional description of the before image.
 * @param afterContentDescription optional description of the after image.
 * @param progressStep fraction used for keyboard and accessibility progress steps.
 * @param zoomEnabled when `true`, pinch, two-finger pan, and scroll-wheel zoom
 * both images together. One-finger drag still moves the divider at 1x, or when
 * the pointer starts near the divider while zoomed.
 * @param zoomState shared zoom and pan; defaults to [rememberCompareZoomState].
 */
@Suppress("ParamsComparedByRef")
@Composable
fun BeforeAfterSlider(
    before: Painter,
    after: Painter,
    modifier: Modifier = Modifier,
    orientation: SliderOrientation = SliderOrientation.Horizontal,
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
    thumbPainter: Painter? = null,
    thumbIconPadding: Dp = CompareKitDefaults.ThumbIconPadding,
    thumbContent: (@Composable (Modifier) -> Unit)? = null,
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
    animationMode: CompareAnimationMode = CompareKitDefaults.AutoScrubAnimationMode,
    autoScrubDurationMillis: Int = CompareKitDefaults.AutoScrubDurationMillis,
    pauseAutoScrubOnInteraction: Boolean = CompareKitDefaults.PauseAutoScrubOnInteraction,
    contentDescription: String = CompareKitDefaults.ContentDescription,
    beforeContentDescription: String? = null,
    afterContentDescription: String? = null,
    progressStep: Float = CompareKitDefaults.ProgressStep,
    zoomEnabled: Boolean = CompareKitDefaults.ZoomEnabled,
    zoomState: CompareZoomState = rememberCompareZoomState(),
) {
    BeforeAfterSlider(
        before = CompareImageSource.painter(before),
        after = CompareImageSource.painter(after),
        modifier = modifier,
        orientation = orientation,
        contentScale = contentScale,
        sliderPosition = sliderPosition,
        state = state,
        dividerColor = dividerColor,
        dividerThickness = dividerThickness,
        thumbColor = thumbColor,
        thumbSize = thumbSize,
        thumbShape = thumbShape,
        thumbBorderColor = thumbBorderColor,
        thumbBorderWidth = thumbBorderWidth,
        thumbPainter = thumbPainter,
        thumbIconPadding = thumbIconPadding,
        thumbContent = thumbContent,
        cornerRadius = cornerRadius,
        backgroundColor = backgroundColor,
        showDivider = showDivider,
        showThumb = showThumb,
        enabled = enabled,
        doubleTapToReset = doubleTapToReset,
        defaultSliderPosition = defaultSliderPosition,
        onDoubleTap = onDoubleTap,
        onLongPress = onLongPress,
        beforeLabel = beforeLabel,
        afterLabel = afterLabel,
        showBeforeLabel = showBeforeLabel,
        showAfterLabel = showAfterLabel,
        labelAlignment = labelAlignment,
        labelPadding = labelPadding,
        labelOffset = labelOffset,
        animationMode = animationMode,
        autoScrubDurationMillis = autoScrubDurationMillis,
        pauseAutoScrubOnInteraction = pauseAutoScrubOnInteraction,
        contentDescription = contentDescription,
        beforeContentDescription = beforeContentDescription,
        afterContentDescription = afterContentDescription,
        progressStep = progressStep,
        zoomEnabled = zoomEnabled,
        zoomState = zoomState,
    )
}

/**
 * A before/after image comparison slider that loads images from [CompareImageSource].
 *
 * Use [CompareImageSource.model] or [CompareImageSource.url] with Coil-supported values
 * such as URLs, files, drawables, and bitmaps. Local [Painter] instances are supported via
 * [CompareImageSource.painter].
 */
@Suppress("ParamsComparedByRef")
@Composable
fun BeforeAfterSlider(
    before: CompareImageSource,
    after: CompareImageSource,
    modifier: Modifier = Modifier,
    orientation: SliderOrientation = SliderOrientation.Horizontal,
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
    thumbPainter: Painter? = null,
    thumbIconPadding: Dp = CompareKitDefaults.ThumbIconPadding,
    thumbContent: (@Composable (Modifier) -> Unit)? = null,
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
    beforePlaceholder: CompareImageSource? = null,
    beforeError: CompareImageSource? = null,
    afterPlaceholder: CompareImageSource? = null,
    afterError: CompareImageSource? = null,
    animationMode: CompareAnimationMode = CompareKitDefaults.AutoScrubAnimationMode,
    autoScrubDurationMillis: Int = CompareKitDefaults.AutoScrubDurationMillis,
    pauseAutoScrubOnInteraction: Boolean = CompareKitDefaults.PauseAutoScrubOnInteraction,
    contentDescription: String = CompareKitDefaults.ContentDescription,
    beforeContentDescription: String? = null,
    afterContentDescription: String? = null,
    progressStep: Float = CompareKitDefaults.ProgressStep,
    zoomEnabled: Boolean = CompareKitDefaults.ZoomEnabled,
    zoomState: CompareZoomState = rememberCompareZoomState(),
) {
    val beforePainter = rememberCompareImagePainter(
        source = before,
        placeholder = beforePlaceholder,
        error = beforeError,
    )
    val afterPainter = rememberCompareImagePainter(
        source = after,
        placeholder = afterPlaceholder,
        error = afterError,
    )

    BeforeAfterSliderLayout(
        before = beforePainter,
        after = afterPainter,
        modifier = modifier,
        orientation = orientation,
        contentScale = contentScale,
        state = state,
        dividerColor = dividerColor,
        dividerThickness = dividerThickness,
        thumbColor = thumbColor,
        thumbSize = thumbSize,
        thumbShape = thumbShape,
        thumbBorderColor = thumbBorderColor,
        thumbBorderWidth = thumbBorderWidth,
        thumbPainter = thumbPainter,
        thumbIconPadding = thumbIconPadding,
        thumbContent = thumbContent,
        cornerRadius = cornerRadius,
        backgroundColor = backgroundColor,
        showDivider = showDivider,
        showThumb = showThumb,
        enabled = enabled,
        doubleTapToReset = doubleTapToReset,
        defaultSliderPosition = defaultSliderPosition,
        onDoubleTap = onDoubleTap,
        onLongPress = onLongPress,
        beforeLabel = beforeLabel,
        afterLabel = afterLabel,
        showBeforeLabel = showBeforeLabel,
        showAfterLabel = showAfterLabel,
        labelAlignment = labelAlignment,
        labelPadding = labelPadding,
        labelOffset = labelOffset,
        animationMode = animationMode,
        autoScrubDurationMillis = autoScrubDurationMillis,
        pauseAutoScrubOnInteraction = pauseAutoScrubOnInteraction,
        contentDescription = contentDescription,
        beforeContentDescription = beforeContentDescription,
        afterContentDescription = afterContentDescription,
        progressStep = progressStep,
        zoomEnabled = zoomEnabled,
        zoomState = zoomState,
    )
}

@Suppress("ParamsComparedByRef")
@Composable
private fun BeforeAfterSliderLayout(
    before: Painter,
    after: Painter,
    modifier: Modifier,
    orientation: SliderOrientation,
    contentScale: ContentScale,
    state: SliderState,
    dividerColor: Color,
    dividerThickness: Dp,
    thumbColor: Color,
    thumbSize: Dp,
    thumbShape: Shape,
    thumbBorderColor: Color,
    thumbBorderWidth: Dp,
    thumbPainter: Painter?,
    thumbIconPadding: Dp,
    thumbContent: (@Composable (Modifier) -> Unit)?,
    cornerRadius: Dp,
    backgroundColor: Color,
    showDivider: Boolean,
    showThumb: Boolean,
    enabled: Boolean,
    doubleTapToReset: Boolean,
    defaultSliderPosition: Float,
    onDoubleTap: (() -> Unit)?,
    onLongPress: (() -> Unit)?,
    beforeLabel: (@Composable () -> Unit)?,
    afterLabel: (@Composable () -> Unit)?,
    showBeforeLabel: Boolean,
    showAfterLabel: Boolean,
    labelAlignment: CompareLabelAlignment,
    labelPadding: Dp,
    labelOffset: DpOffset,
    animationMode: CompareAnimationMode,
    autoScrubDurationMillis: Int,
    pauseAutoScrubOnInteraction: Boolean,
    contentDescription: String,
    beforeContentDescription: String?,
    afterContentDescription: String?,
    progressStep: Float,
    zoomEnabled: Boolean,
    zoomState: CompareZoomState,
) {
    var layoutSize by remember { mutableStateOf(IntSize.Zero) }
    var isUserInteracting by remember { mutableStateOf(false) }
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
    val resolvedContentDescription = remember(
        contentDescription,
        beforeContentDescription,
        afterContentDescription,
    ) {
        resolveCompareSliderContentDescription(
            contentDescription = contentDescription,
            beforeContentDescription = beforeContentDescription,
            afterContentDescription = afterContentDescription,
        )
    }
    val zoomHitSlopPx = remember(density, thumbSize) {
        with(density) {
            max(thumbSize.toPx() / 2f + 16.dp.toPx(), ZoomDefaults.SliderHitSlop.toPx())
        }
    }
    val zoomScale = if (zoomEnabled) zoomState.scale else 1f
    val zoomOffsetX = if (zoomEnabled) zoomState.offsetX else 0f
    val zoomOffsetY = if (zoomEnabled) zoomState.offsetY else 0f
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

    SliderAutoScrubEffect(
        state = state,
        animationMode = animationMode,
        enabled = enabled,
        isUserInteracting = pauseAutoScrubOnInteraction && isUserInteracting,
        durationMillis = autoScrubDurationMillis,
    )

    Box(
        modifier = modifier
            .clip(clipShape)
            .background(backgroundColor)
            .onSizeChanged { size ->
                layoutSize = size
                if (zoomEnabled) {
                    zoomState.coerceToViewport(size)
                }
            }
            .compareSliderSemantics(
                state = state,
                contentDescription = resolvedContentDescription,
                enabled = enabled,
            )
            .compareSliderKeyboard(
                state = state,
                orientation = orientation,
                enabled = enabled,
                progressStep = progressStep,
            )
            .sliderGestures(
                state = state,
                sizeProvider = { layoutSize },
                orientation = orientation,
                enabled = enabled,
                doubleTapToReset = doubleTapToReset,
                defaultSliderPosition = defaultSliderPosition,
                onDoubleTap = handleDoubleTap,
                onLongPress = handleLongPress,
                onInteractionStart = { isUserInteracting = true },
                onInteractionEnd = { isUserInteracting = false },
                zoomEnabled = zoomEnabled,
                zoomState = zoomState,
                zoomHitSlopPx = zoomHitSlopPx,
            ),
    ) {
        CompareImageViewer(
            painter = before,
            contentDescription = null,
            contentScale = contentScale,
            modifier = Modifier
                .fillMaxSize()
                .compareImageWindow(
                    progress = state.progress,
                    orientation = orientation,
                    isBefore = true,
                    scale = zoomScale,
                    offsetX = zoomOffsetX,
                    offsetY = zoomOffsetY,
                ),
        )

        CompareImageViewer(
            painter = after,
            contentDescription = null,
            contentScale = contentScale,
            modifier = Modifier
                .fillMaxSize()
                .compareImageWindow(
                    progress = state.progress,
                    orientation = orientation,
                    isBefore = false,
                    scale = zoomScale,
                    offsetX = zoomOffsetX,
                    offsetY = zoomOffsetY,
                ),
        )

        if (showDivider) {
            SliderDivider(
                modifier = Modifier
                    .align(sliderDividerAlignment(orientation))
                    .offset {
                        sliderDividerOffset(
                            layoutSize = layoutSize,
                            progress = state.progress,
                            orientation = orientation,
                            dividerThicknessPx = dividerThicknessPx,
                        )
                    },
                orientation = orientation,
                thickness = dividerThickness,
                color = effectiveDividerColor,
            )
        }

        if (showThumb) {
            SliderThumb(
                modifier = Modifier
                    .align(sliderDividerAlignment(orientation))
                    .offset {
                        sliderThumbOffset(
                            layoutSize = layoutSize,
                            progress = state.progress,
                            orientation = orientation,
                            thumbSizePx = thumbSizePx,
                        )
                    },
                size = thumbSize,
                color = effectiveThumbColor,
                shape = thumbShape,
                borderWidth = thumbBorderWidth,
                borderColor = effectiveThumbBorderColor,
                painter = thumbPainter,
                painterPadding = thumbIconPadding,
                content = thumbContent,
            )
        }

        CompareSliderLabels(
            showBeforeLabel = showBeforeLabel,
            showAfterLabel = showAfterLabel,
            alignment = labelAlignment,
            orientation = orientation,
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

/**
 * Clips to the before or after half in viewport space, then draws the image
 * with a top-start zoom/pan transform so the divider stays on screen.
 */
private fun Modifier.compareImageWindow(
    progress: Float,
    orientation: SliderOrientation,
    isBefore: Boolean,
    scale: Float,
    offsetX: Float,
    offsetY: Float,
): Modifier = drawWithContent {
    val clipBounds = sliderClipBounds(size, progress, orientation)
    clipRect(
        left = if (isBefore) clipBounds.beforeLeft else clipBounds.afterLeft,
        top = if (isBefore) clipBounds.beforeTop else clipBounds.afterTop,
        right = if (isBefore) clipBounds.beforeRight else clipBounds.afterRight,
        bottom = if (isBefore) clipBounds.beforeBottom else clipBounds.afterBottom,
    ) {
        translate(left = offsetX, top = offsetY) {
            scale(scaleX = scale, scaleY = scale, pivot = Offset.Zero) {
                this@drawWithContent.drawContent()
            }
        }
    }
}
