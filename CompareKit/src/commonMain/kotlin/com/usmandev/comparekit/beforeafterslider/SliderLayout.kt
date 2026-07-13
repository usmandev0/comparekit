package com.usmandev.comparekit.beforeafterslider

import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import kotlin.math.roundToInt

internal data class SliderClipBounds(
    val beforeLeft: Float,
    val beforeTop: Float,
    val beforeRight: Float,
    val beforeBottom: Float,
    val afterLeft: Float,
    val afterTop: Float,
    val afterRight: Float,
    val afterBottom: Float,
)

internal fun sliderClipBounds(
    size: Size,
    progress: Float,
    orientation: SliderOrientation,
): SliderClipBounds {
    return when (orientation) {
        SliderOrientation.Horizontal -> {
            val clipPosition = size.width * progress
            SliderClipBounds(
                beforeLeft = clipPosition,
                beforeTop = 0f,
                beforeRight = size.width,
                beforeBottom = size.height,
                afterLeft = 0f,
                afterTop = 0f,
                afterRight = clipPosition,
                afterBottom = size.height,
            )
        }

        SliderOrientation.Vertical -> {
            val clipPosition = size.height * progress
            SliderClipBounds(
                beforeLeft = 0f,
                beforeTop = clipPosition,
                beforeRight = size.width,
                beforeBottom = size.height,
                afterLeft = 0f,
                afterTop = 0f,
                afterRight = size.width,
                afterBottom = clipPosition,
            )
        }
    }
}

internal fun sliderDividerOffset(
    layoutSize: IntSize,
    progress: Float,
    orientation: SliderOrientation,
    dividerThicknessPx: Float,
): IntOffset {
    return when (orientation) {
        SliderOrientation.Horizontal -> {
            val x = ((layoutSize.width * progress) - (dividerThicknessPx / 2f)).roundToInt()
            IntOffset(x = x, y = 0)
        }

        SliderOrientation.Vertical -> {
            val y = ((layoutSize.height * progress) - (dividerThicknessPx / 2f)).roundToInt()
            IntOffset(x = 0, y = y)
        }
    }
}

internal fun sliderThumbOffset(
    layoutSize: IntSize,
    progress: Float,
    orientation: SliderOrientation,
    thumbSizePx: Float,
): IntOffset {
    return when (orientation) {
        SliderOrientation.Horizontal -> {
            val x = ((layoutSize.width * progress) - (thumbSizePx / 2f)).roundToInt()
            IntOffset(x = x, y = 0)
        }

        SliderOrientation.Vertical -> {
            val y = ((layoutSize.height * progress) - (thumbSizePx / 2f)).roundToInt()
            IntOffset(x = 0, y = y)
        }
    }
}

internal fun sliderDividerAlignment(orientation: SliderOrientation): Alignment {
    return when (orientation) {
        SliderOrientation.Horizontal -> Alignment.CenterStart
        SliderOrientation.Vertical -> Alignment.TopCenter
    }
}

internal fun sliderPrimaryDimension(
    size: IntSize,
    orientation: SliderOrientation,
): Float {
    return when (orientation) {
        SliderOrientation.Horizontal -> size.width.toFloat()
        SliderOrientation.Vertical -> size.height.toFloat()
    }
}
