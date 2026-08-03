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
- Custom thumb, divider, labels, and gestures
- Auto-scrub animation (PingPong, RevealOnce)
- Coil image loading via `CompareImageSource`
- Hoisted `SliderState` for full control

**Platforms:** Android (`minSdk 24`), iOS (`iosArm64`, `iosSimulatorArm64`)

---

## Installation

Add to your shared `commonMain` dependencies:

```kotlin
implementation("io.github.usmandev0:comparekit:0.1.3")
```

**Requirements:** Kotlin 2.4+, Compose Multiplatform 1.11.1+

<details>
<summary>Version catalog setup</summary>

```toml
# gradle/libs.versions.toml
[versions]
comparekit = "0.1.3"

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

### Gestures

- **Drag** — move the slider
- **Double tap** — reset to center
- **Long press** — `onLongPress` callback

```kotlin
BeforeAfterSlider(
    before = before,
    after = after,
    onDoubleTap = { /* reset feedback */ },
    onLongPress = { /* action */ },
)
```

---

## API at a glance

| API | Purpose |
| --- | --- |
| `BeforeAfterSlider` | Main composable |
| `SliderState` | Hoisted progress (`0f`–`1f`) |
| `rememberCompareSliderState()` | Create state |
| `CompareAnimationMode` | Auto-scrub modes |
| `CompareImageSource` | Coil / Painter images |
| `CompareKitDefaults` | Default styling values |

**Package:** `com.usmandev.comparekit.beforeafterslider`

---

## Sample App

```bash
git clone https://github.com/usmandev0/comparekit.git
cd comparekit
./gradlew :androidApp:installDebug
```

The demo covers basic usage, Coil loading, customization, labels, gestures, orientation, live controls, and auto animation.

---

## Roadmap

| Planned | Status |
| --- | --- |
| Zoom, magnifier, video comparison | Planned |
| Desktop & Web targets | Planned |
| Accessibility improvements | Planned |

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
