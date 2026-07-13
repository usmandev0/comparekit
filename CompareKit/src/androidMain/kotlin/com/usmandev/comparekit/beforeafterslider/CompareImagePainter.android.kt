package com.usmandev.comparekit.beforeafterslider

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import coil3.compose.rememberAsyncImagePainter

@Composable
actual fun rememberCompareImagePainter(
    source: CompareImageSource,
    placeholder: CompareImageSource?,
    error: CompareImageSource?,
): Painter {
    val placeholderPainter = placeholder?.let { rememberNestedCompareImagePainter(it) }
    val errorPainter = error?.let { rememberNestedCompareImagePainter(it) }
    return when (source) {
        is CompareImageSource.Painter -> source.painter
        is CompareImageSource.Model -> rememberAsyncImagePainter(
            model = source.model,
            placeholder = placeholderPainter,
            error = errorPainter,
        )
    }
}

@Composable
private fun rememberNestedCompareImagePainter(source: CompareImageSource): Painter {
    return when (source) {
        is CompareImageSource.Painter -> source.painter
        is CompareImageSource.Model -> rememberAsyncImagePainter(model = source.model)
    }
}
