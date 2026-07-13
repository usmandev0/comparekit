package com.usmandev.comparekit.beforeafterslider

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter

/**
 * Resolves a [CompareImageSource] into a [Painter] suitable for [CompareImageViewer]
 * and [BeforeAfterSlider].
 *
 * [CompareImageSource.Painter] returns the supplied painter directly.
 * [CompareImageSource.Model] is loaded with Coil on platforms where Coil is available.
 */
@Composable
expect fun rememberCompareImagePainter(
    source: CompareImageSource,
    placeholder: CompareImageSource? = null,
    error: CompareImageSource? = null,
): Painter
