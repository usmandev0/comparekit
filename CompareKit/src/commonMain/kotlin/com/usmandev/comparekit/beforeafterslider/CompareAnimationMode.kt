package com.usmandev.comparekit.beforeafterslider

/**
 * Controls automatic slider animation in [BeforeAfterSlider].
 */
enum class CompareAnimationMode {
    /** No automatic animation; the slider is user-controlled only. */
    None,

    /** Continuously animates between fully before and fully after (0 → 1 → 0 → …). */
    PingPong,

    /** Animates once to fully after (1f), then stops. */
    RevealOnce,
}
