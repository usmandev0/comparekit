package com.usmandev.comparekit

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import kotlin.math.roundToInt

/**
 * A before/after image comparison slider built with Compose Multiplatform.
 *
 * The [before] image is drawn fully. The [after] image is clipped horizontally
 * according to [state] progress. A vertical divider and circular thumb follow
 * the drag position.
 *
 * @param before painter for the fully visible "before" image.
 * @param after painter for the "after" image, clipped by slider progress.
 * @param modifier modifier applied to the root container.
 * @param state slider progress state; defaults to 50% via [rememberSliderState].
 */
@Composable
fun BeforeAfterSlider(
    before: Painter,
    after: Painter,
    modifier: Modifier = Modifier,
    state: SliderState = rememberSliderState(),
) {
    var layoutSize by remember { mutableStateOf(IntSize.Zero) }
    val density = LocalDensity.current
    val thumbSizePx = with(density) { SliderDefaults.ThumbSize.toPx() }
    val dividerThicknessPx = with(density) { SliderDefaults.DividerThickness.toPx() }

    Box(
        modifier = modifier
            .onSizeChanged { layoutSize = it }
            .sliderDragGesture(
                state = state,
                sizeProvider = { layoutSize },
            ),
    ) {
        Image(
            painter = before,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )

        Image(
            painter = after,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .drawWithContent {
                    val clipWidth = size.width * state.progress
                    clipRect(
                        left = 0f,
                        top = 0f,
                        right = clipWidth,
                        bottom = size.height,
                    ) {
                        this@drawWithContent.drawContent()
                    }
                },
        )

        val dividerOffsetX = ((layoutSize.width * state.progress) - (dividerThicknessPx / 2f))
            .roundToInt()
        SliderDivider(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset { IntOffset(x = dividerOffsetX, y = 0) },
        )

        val thumbOffsetX = ((layoutSize.width * state.progress) - (thumbSizePx / 2f))
            .roundToInt()
        SliderThumb(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset { IntOffset(x = thumbOffsetX, y = 0) },
        )
    }
}
