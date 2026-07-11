package com.usmandev.comparekit.sample.demo

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.usmandev.comparekit.sample.R

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
