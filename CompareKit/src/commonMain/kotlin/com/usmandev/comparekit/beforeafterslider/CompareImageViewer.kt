package com.usmandev.comparekit.beforeafterslider

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale

/**
 * Displays an image from a [CompareImageSource].
 *
 * Supports local [Painter] instances and Coil-backed models such as URLs, files,
 * drawables, and bitmaps via [rememberCompareImagePainter].
 */
@Composable
fun CompareImageViewer(
    source: CompareImageSource,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    placeholder: CompareImageSource? = null,
    error: CompareImageSource? = null,
) {
    val painter = rememberCompareImagePainter(
        source = source,
        placeholder = placeholder,
        error = error,
    )
    CompareImageViewer(
        painter = painter,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale,
    )
}

/**
 * Displays an image from a resolved [Painter].
 */
@Suppress("ParamsComparedByRef")
@Composable
fun CompareImageViewer(
    painter: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
) {
    Image(
        painter = painter,
        contentDescription = contentDescription,
        contentScale = contentScale,
        modifier = modifier,
    )
}
