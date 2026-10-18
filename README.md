<p align="center">
  <img src="images/comparekit-logo.png" alt="CompareKit logo" width="360" />
</p>

<p align="center">
  <strong>CompareKit</strong><br />
  Before/after image comparison for Kotlin Multiplatform
</p>

<p align="center">
  <a href="https://central.sonatype.com/artifact/io.github.usmandev0/comparekit">Maven Central</a> ·
  <a href="#installation">Install</a> ·
  <a href="#quick-start">Quick Start</a> ·
  <a href="CHANGELOG.md">Changelog</a> ·
  <a href="LICENSE">License</a>
</p>

---

## What is CompareKit?

A Compose Multiplatform library for interactive before/after image sliders. One composable, Android + iOS, no platform-specific UI code.

**Highlights**

- Horizontal and vertical sliders
- Synced pinch-zoom and pan (both images stay aligned)
- Custom thumb, divider, labels, and gestures
- Auto-scrub animation (PingPong, RevealOnce)
- Coil image loading via `CompareImageSource`
- Hoisted `SliderState` for full control
- Accessibility: slider semantics, progress, and keyboard navigation

**Platforms:** Android (`minSdk 24`), iOS (`iosArm64`, `iosSimulatorArm64`)

---

## Installation

Add to your shared `commonMain` dependencies:

```kotlin
implementation("io.github.usmandev0:comparekit:0.1.4")
```

**Requirements:** Kotlin 2.4+, Compose Multiplatform 1.11.1+

<details>
<summary>Version catalog setup</summary>

```toml
# gradle/libs.versions.toml
[versions]
comparekit = "0.1.4"

[libraries]
comparekit = { module = "io.github.usmandev0:comparekit", version.ref = "comparekit" }
```

```kotlin
// build.gradle.kts
commonMain.dependencies {
    implementation(libs.comparekit)
}
```

</details>

---

## Quick Start

```kotlin
@Composable
fun PhotoComparison(before: Painter, after: Painter) {
    BeforeAfterSlider(
        before = before,
        after = after,
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp),
    )
}
```

Drag to compare. Starts at 50% by default.

---

## Common Options

### With state

```kotlin
val state = rememberCompareSliderState(initialProgress = 0.5f)

BeforeAfterSlider(
    before = before,
    after = after,
    state = state,
)
```

### Custom look

```kotlin
BeforeAfterSlider(
    before = before,
    after = after,
    cornerRadius = 12.dp,
    dividerColor = Color.White,
    thumbSize = 40.dp,
    beforeLabel = { CompareLabel(text = "Before") },
    afterLabel = { CompareLabel(text = "After") },
)
```

### Vertical slider

```kotlin
BeforeAfterSlider(
    orientation = SliderOrientation.Vertical,
    before = before,
    after = after,
    modifier = Modifier.fillMaxWidth().height(240.dp),
)
```

### Auto animation

```kotlin
BeforeAfterSlider(
    before = before,
    after = after,
    animationMode = CompareAnimationMode.PingPong,
    autoScrubDurationMillis = 2500,
    pauseAutoScrubOnInteraction = true,
)
```

| Mode | What it does |
| --- | --- |
| `None` | No auto animation (default) |
| `PingPong` | Loops 0 → 1 → 0 |
| `RevealOnce` | Animates to after once, then stops |

### Load images with Coil

```kotlin
BeforeAfterSlider(
    before = CompareImageSource.url("https://example.com/before.jpg"),
    after = CompareImageSource.url("https://example.com/after.jpg"),
)
```

### Zoom and pan

Pinch (or use the scroll wheel) to magnify both images with the same transform. While zoomed, drag the image to pan; drag near the divider to keep comparing. Double-tap resets zoom, then a second double-tap resets the slider.

```kotlin
val zoomState = rememberCompareZoomState(maxScale = 4f)

BeforeAfterSlider(
    before = before,
    after = after,
    zoomEnabled = true,
    zoomState = zoomState,
)
```

Pass `zoomEnabled = false` to keep the original drag-only behavior.

### Gestures

- **Drag** — move the slider (or pan when zoomed, away from the divider)
- **Pinch / scroll wheel** — zoom both images together
- **Double tap** — reset zoom if magnified, otherwise reset the slider to center
- **Long press** — `onLongPress` callback

```kotlin
BeforeAfterSlider(
    before = before,
    after = after,
    onDoubleTap = { /* reset feedback */ },
    onLongPress = { /* action */ },
)
```

### Accessibility

The slider exposes progress semantics (`progressBarRangeInfo`, `setProgress`, `stateDescription`) and keyboard support (arrows ± step, Home/End). Images stay decorative; describe them via optional params when needed.

```kotlin
BeforeAfterSlider(
    before = before,
    after = after,
    contentDescription = "Photo retouch comparison",
    beforeContentDescription = "Original photo",
    afterContentDescription = "Edited photo",
    progressStep = 0.05f,
)
```

---

## API at a glance

| API | Purpose |
| --- | --- |
| `BeforeAfterSlider` | Main composable |
| `SliderState` | Hoisted progress (`0f`–`1f`) |
| `rememberCompareSliderState()` | Create slider state |
| `CompareZoomState` | Shared scale and pan for both images |
| `rememberCompareZoomState()` | Create zoom state |
| `CompareAnimationMode` | Auto-scrub modes |
| `CompareImageSource` | Coil / Painter images |
| `CompareKitDefaults` | Default styling and a11y values |

**Package:** `com.usmandev.comparekit.beforeafterslider`

---

## Sample App

```bash
git clone https://github.com/usmandev0/comparekit.git
cd comparekit
./gradlew :androidApp:installDebug
```

The demo covers basic usage, Coil loading, customization, labels, gestures, orientation, live controls, auto animation, accessibility, and synced zoom/pan.

---

## Roadmap

| Planned | Status |
| --- | --- |
| Pinch-zoom and pan | Done |
| Magnifier, video comparison | Planned |
| Desktop & Web targets | Planned |
| Accessibility improvements | Done |

See [CHANGELOG.md](CHANGELOG.md) for release history.

---

## Contributing

1. Fork the repo
2. Create a feature branch
3. Open a Pull Request

```bash
./gradlew :CompareKit:compileAndroidMain
./gradlew :CompareKit:compileKotlinIosSimulatorArm64
```

---

## License

Apache License 2.0 — see [LICENSE](LICENSE).

**Author:** [Usman (@usmandev0)](https://github.com/usmandev0)

If CompareKit helps your project, consider starring the repo on [GitHub](https://github.com/usmandev0/comparekit).
