package com.usmandev.comparekit.beforeafterslider.defaults

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import com.usmandev.comparekit.beforeafterslider.CompareAnimationMode

internal object AnimationDefaults {
    val AutoScrubMode: CompareAnimationMode = CompareAnimationMode.None
    const val AutoScrubDurationMillis: Int = 2000
    const val PauseAutoScrubOnInteraction: Boolean = true
}

internal fun autoScrubAnimationSpec(durationMillis: Int): AnimationSpec<Float> {
    return tween(
        durationMillis = durationMillis.coerceAtLeast(1),
        easing = LinearEasing,
    )
}
