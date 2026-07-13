package com.usmandev.comparekit.beforeafterslider

import androidx.compose.ui.graphics.painter.Painter as ComposePainter

/**
 * Describes an image that [CompareImageViewer] can display.
 *
 * Use [Painter] when you already have a Compose [ComposePainter], or [Model] for any
 * Coil-supported value such as a URL, file path, `Uri`, drawable resource ID,
 * byte array, or bitmap.
 */
sealed interface CompareImageSource {
    /** A ready-to-use Compose [ComposePainter]. */
    data class Painter(val painter: ComposePainter) : CompareImageSource

    /** Any Coil-supported image model. */
    data class Model(val model: Any?) : CompareImageSource

    companion object {
        /** Wraps an existing [ComposePainter]. */
        fun painter(painter: ComposePainter): CompareImageSource = Painter(painter)

        /** Wraps any Coil-supported image model. */
        fun model(model: Any?): CompareImageSource = Model(model)

        /** Loads an image from a remote or local URL string. */
        fun url(url: String): CompareImageSource = Model(url)
    }
}
