package com.usmandev.comparekit.beforeafterslider

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

/**
 * Drives automatic slider progress animation for [BeforeAfterSlider].
 *
 * If multiple sliders share the same [SliderState], enable auto-scrub on only one
 * composable to avoid conflicting animation loops.
 */
@Composable
internal fun SliderAutoScrubEffect(
    state: SliderState,
    animationMode: CompareAnimationMode,
    enabled: Boolean,
    isUserInteracting: Boolean,
    durationMillis: Int,
) {
    LaunchedEffect(state, animationMode, enabled, isUserInteracting, durationMillis) {
        if (animationMode == CompareAnimationMode.None || !enabled || isUserInteracting) {
            return@LaunchedEffect
        }

        val animationSpec = autoScrubAnimationSpec(durationMillis)
        val animatable = Animatable(state.progress)

        when (animationMode) {
            CompareAnimationMode.None -> Unit

            CompareAnimationMode.RevealOnce -> {
                animatable.snapTo(state.progress)
                animatable.animateTo(
                    targetValue = SliderState.MaxProgress,
                    animationSpec = animationSpec,
                ) {
                    state.updateProgress(value)
                }
            }

            CompareAnimationMode.PingPong -> {
                while (true) {
                    animatable.snapTo(state.progress)
                    animatable.animateTo(
                        targetValue = SliderState.MaxProgress,
                        animationSpec = animationSpec,
                    ) {
                        state.updateProgress(value)
                    }
                    animatable.animateTo(
                        targetValue = SliderState.MinProgress,
                        animationSpec = animationSpec,
                    ) {
                        state.updateProgress(value)
                    }
                }
            }
        }
    }
}

internal fun autoScrubAnimationSpec(durationMillis: Int): AnimationSpec<Float> {
    return tween(
        durationMillis = durationMillis.coerceAtLeast(1),
        easing = LinearEasing,
    )
}
