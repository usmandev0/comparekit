package com.usmandev.comparekit.app.demo

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.usmandev.comparekit.app.R
import com.usmandev.comparekit.beforeafterslider.CompareImageSource

/** Remote demo image URLs loaded with Coil via [CompareImageSource]. */
internal const val DemoCoilBeforeUrl = "https://picsum.photos/id/1015/800/560"
internal const val DemoCoilAfterUrl = "https://picsum.photos/id/1018/800/560"

/**
 * Provides the "before" demo image.
 */
@Composable
fun rememberDemoBeforePainter(): Painter {
    return painterResource(id = R.drawable.before)
}

/**
 * Provides the "after" demo image.
 */
@Composable
fun rememberDemoAfterPainter(): Painter {
    return painterResource(id = R.drawable.after)
}

/**
 * Provides the "before" demo image loaded from a remote URL with Coil.
 */
@Composable
fun rememberDemoCoilBeforeSource(): CompareImageSource {
    return CompareImageSource.url(DemoCoilBeforeUrl)
}

/**
 * Provides the "after" demo image loaded from a remote URL with Coil.
 */
@Composable
fun rememberDemoCoilAfterSource(): CompareImageSource {
    return CompareImageSource.url(DemoCoilAfterUrl)
}

/**
 * Provides the compare-arrows icon used on the demo thumb.
 */
@Composable
fun rememberDemoThumbIcon(): Painter {
    return painterResource(id = R.drawable.ic_compare_arrows)
}
