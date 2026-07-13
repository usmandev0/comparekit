<p align="center">
  <img src="images/comparekit-logo.png" alt="CompareKit logo" width="200" height="200" />
</p>

<p align="center">
  <strong>Beautiful, interactive before/after image comparison for Kotlin Multiplatform.</strong>
</p>

<p align="center">
  <a href="https://central.sonatype.com/artifact/io.github.usmandev0/comparekit">Maven Central</a> ·
  <a href="https://kotlinlang.org">Kotlin</a> ·
  <a href="https://www.jetbrains.com/compose-multiplatform/">Compose Multiplatform</a> ·
  <a href="https://kotlinlang.org/docs/multiplatform.html">Platform</a> ·
  <a href="LICENSE">License</a> ·
  <a href="https://github.com/usmandev0/comparekit/stargazers">GitHub Stars</a>
</p>

<p align="center">
  <em>A modern Compose Multiplatform library for polished before/after image sliders.</em>
</p>

<p align="center">
  <a href="#installation"><strong>Installation</strong></a> ·
  <a href="#usage"><strong>Quick Start</strong></a> ·
  <a href="#usage"><strong>Documentation</strong></a> ·
  <a href="#changelog"><strong>Changelog</strong></a> ·
  <a href="#sample-app"><strong>Sample App</strong></a> ·
  <a href="#contributing"><strong>Contributing</strong></a>
</p>

<p align="center">
  <a href="README_SHORT.md">Short README</a> ·
  <a href="CHANGELOG.md">Changelog file</a> ·
  <a href="androidApp/README.md">Sample README</a>
</p>

---

---

---

## Features


|     | Feature                                                                 |
| --- | ----------------------------------------------------------------------- |
| ✅   | **Kotlin Multiplatform** — share one UI implementation across platforms |
| ✅   | **Compose Multiplatform** — declarative, reactive UI built with Compose |
| ✅   | **Android** — `minSdk 24+`                                              |
| ✅   | **iOS** — `iosArm64`, `iosSimulatorArm64`                               |
| ✅   | **Horizontal Slider** — smooth left/right before/after comparison       |
| ✅   | **Vertical Slider** — smooth top/bottom before/after comparison         |
| ✅   | **Custom Thumb** — size, shape, color, and border                       |
| ✅   | **Custom Divider** — color and thickness                                |
| ✅   | **Labels** — default, custom composable, and alignment options          |
| ✅   | **Gestures** — drag, double-tap reset, and long press                   |
| ✅   | **Double Tap Reset** — animated return to a default position            |
| ✅   | **Long Press** — callback for contextual actions                        |
| ✅   | **State API** — hoisted `SliderState` for full control                  |
| ✅   | **Material 3–inspired defaults** — polished out of the box              |
| ✅   | **Lightweight** — focused API with minimal dependencies                 |
| ✅   | **No Platform-Specific Code** — 100% `commonMain` implementation        |


> **Note:** CompareKit ships no Android- or iOS-specific UI code. Everything lives in `commonMain`.

---

## Installation

Add CompareKit to your **shared** `commonMain` source set.

### Version Catalog

```toml
# gradle/libs.versions.toml

[versions]
comparekit = "0.1.2"

[libraries]
comparekit = { module = "io.github.usmandev0:comparekit", version.ref = "comparekit" }
```

```kotlin
// build.gradle.kts

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.comparekit)
        }
    }
}
```

### Gradle Kotlin DSL

```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation("io.github.usmandev0:comparekit:0.1.2")
        }
    }
}
```

**Maven coordinates**

```text
io.github.usmandev0:comparekit:0.1.2
```

### Requirements


| Dependency            | Minimum Version |
| --------------------- | --------------- |
| Kotlin                | `2.4.0`         |
| Compose Multiplatform | `1.11.1`        |
| Android `minSdk`      | `24`            |


Your project must already use Compose Multiplatform for Android and/or iOS.

---

## Usage

### Quick Start

Provide two `Painter` instances — one for the **before** image and one for the **after** image — and place `BeforeAfterSlider` in your composable hierarchy.

```kotlin
@Composable
fun PhotoComparison(
    before: Painter,
    after: Painter,
) {
    BeforeAfterSlider(
        before = before,
        after = after,
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp),
    )
}
```

The slider starts at **50%** by default. Drag horizontally to reveal the after image.

---

### Basic Example

```kotlin
@Composable
fun BasicComparison(before: Painter, after: Painter) {
    val state = rememberCompareSliderState(initialProgress = 0.5f)

    BeforeAfterSlider(
        before = before,
        after = after,
        state = state,
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
    )
}
```

---

### Advanced Example

Combine customization, labels, and gestures in a single slider.

```kotlin
@Composable
fun AdvancedComparison(
    before: Painter,
    after: Painter,
    onReset: () -> Unit,
    onLongPress: () -> Unit,
) {
    val state = rememberCompareSliderState()

    BeforeAfterSlider(
        before = before,
        after = after,
        state = state,
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp),
        cornerRadius = 16.dp,
        dividerColor = Color.White,
        dividerThickness = 3.dp,
        thumbColor = MaterialTheme.colorScheme.primary,
        thumbSize = 40.dp,
        thumbShape = CircleShape,
        beforeLabel = { CompareLabel(text = "Original") },
        afterLabel = {
            Text(
                text = "Edited",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White,
            )
        },
        labelAlignment = CompareLabelAlignment.TopStart,
        doubleTapToReset = true,
        defaultSliderPosition = CompareKitDefaults.SliderPosition,
        onDoubleTap = onReset,
        onLongPress = onLongPress,
    )
}
```

---

### Customization

Every visual aspect of the slider has a sensible default via `CompareKitDefaults` and can be overridden per composable.

```kotlin
BeforeAfterSlider(
    before = beforePainter,
    after = afterPainter,
    dividerColor = Color.White,
    dividerThickness = 4.dp,
    thumbColor = Color(0xFF6750A4),
    thumbSize = 36.dp,
    thumbBorderColor = Color.Black.copy(alpha = 0.2f),
    thumbBorderWidth = 2.dp,
    cornerRadius = 12.dp,
    backgroundColor = Color.Black,
    showDivider = true,
    showThumb = true,
    enabled = true,
)
```


| Parameter          | Default                           | Description                        |
| ------------------ | --------------------------------- | ---------------------------------- |
| `dividerColor`     | `CompareKitDefaults.DividerColor` | Color of the divider line          |
| `dividerThickness` | `2.dp`                            | Width of the divider               |
| `thumbColor`       | `Color.White`                     | Fill color of the thumb            |
| `thumbSize`        | `32.dp`                           | Diameter of the thumb              |
| `thumbShape`       | `CircleShape`                     | Shape of the thumb                 |
| `thumbBorderColor` | Semi-transparent black            | Thumb outline color                |
| `thumbBorderWidth` | `1.dp`                            | Thumb outline width                |
| `cornerRadius`     | `0.dp`                            | Corner radius clipping both images |
| `backgroundColor`  | `Transparent`                     | Background behind images           |
| `showDivider`      | `true`                            | Toggle divider visibility          |
| `showThumb`        | `true`                            | Toggle thumb visibility            |
| `enabled`          | `true`                            | Enable or disable all gestures     |


When `enabled = false`, gestures are ignored and the divider and thumb render with reduced alpha.

---

### Labels

Add optional **Before** and **After** labels with default styling or fully custom composables.

#### Default labels

```kotlin
BeforeAfterSlider(
    before = beforePainter,
    after = afterPainter,
    // Defaults to "Before" and "After" labels
)
```

#### Custom labels

```kotlin
BeforeAfterSlider(
    before = beforePainter,
    after = afterPainter,
    beforeLabel = { CompareLabel(text = "Original") },
    afterLabel = { Text("Edited", color = Color.White) },
    labelAlignment = CompareLabelAlignment.BottomStart,
    labelPadding = 12.dp,
)
```

#### Hide labels

```kotlin
BeforeAfterSlider(
    before = beforePainter,
    after = afterPainter,
    showBeforeLabel = false,
    showAfterLabel = false,
)
```

#### Label alignments


| Alignment      | Before label                 | After label                   |
| -------------- | ---------------------------- | ----------------------------- |
| `TopStart`     | Top-start of left region     | Top-end of right region       |
| `TopCenter`    | Top-center of left region    | Top-center of right region    |
| `TopEnd`       | Top-end of left region       | Top-start of right region     |
| `BottomStart`  | Bottom-start of left region  | Bottom-end of right region    |
| `BottomCenter` | Bottom-center of left region | Bottom-center of right region |
| `BottomEnd`    | Bottom-end of left region    | Bottom-start of right region  |


Labels are drawn above images, respect corner radius clipping, and do not block slider dragging.

---

### Gestures

CompareKit supports rich touch interaction out of the box.


| Gesture        | Behavior                                                 |
| -------------- | -------------------------------------------------------- |
| **Drag**       | Smooth horizontal dragging; position clamped to `0f..1f` |
| **Double tap** | Animates slider back to `defaultSliderPosition`          |
| **Long press** | Invokes `onLongPress` callback                           |


```kotlin
BeforeAfterSlider(
    before = beforePainter,
    after = afterPainter,
    doubleTapToReset = true,
    defaultSliderPosition = 0.5f,
    onDoubleTap = { println("Double tap — slider reset") },
    onLongPress = { println("Long press detected") },
    enabled = true,
)
```

Set `enabled = false` to disable all gestures.

---

### Vertical Slider

Use `SliderOrientation.Vertical` to compare images along the vertical axis. The after image appears above the divider and the before image below it. Drag up or down to reveal more of either image.

```kotlin
BeforeAfterSlider(
    orientation = SliderOrientation.Vertical,
    before = beforePainter,
    after = afterPainter,
    modifier = Modifier
        .fillMaxWidth()
        .height(240.dp),
)
```

Horizontal is the default when `orientation` is omitted:

```kotlin
BeforeAfterSlider(
    orientation = SliderOrientation.Horizontal, // default
    before = beforePainter,
    after = afterPainter,
)
```

---

### State API

Hoist slider progress with `SliderState` for programmatic control and observation.

```kotlin
@Composable
fun StatefulComparison(before: Painter, after: Painter) {
    val state = rememberCompareSliderState(initialProgress = 0.3f)

    BeforeAfterSlider(
        before = before,
        after = after,
        state = state,
    )

    // Read current progress
    val progress = state.progress // 0f = before only, 1f = after fully revealed

    // Update programmatically
    state.updateProgress(0.75f)
}
```


| `SliderState` API             | Description                           |
| ----------------------------- | ------------------------------------- |
| `progress`                    | Current position in `0f..1f`          |
| `updateProgress(value)`       | Sets progress, clamped to valid range |
| `MinProgress` / `MaxProgress` | Constants `0f` and `1f`               |
| `DefaultProgress`             | Default starting position `0.5f`      |


---

## Configuration

### `CompareKitDefaults`


| Property           | Value                     |
| ------------------ | ------------------------- |
| `SliderPosition`   | `0.5f`                    |
| `DividerColor`     | `Color.White`             |
| `DividerThickness` | `2.dp`                    |
| `ThumbColor`       | `Color.White`             |
| `ThumbSize`        | `32.dp`                   |
| `ThumbShape`       | `CircleShape`             |
| `ThumbBorderColor` | `Color.Black` @ 12% alpha |
| `ThumbBorderWidth` | `1.dp`                    |
| `CornerRadius`     | `0.dp`                    |
| `BackgroundColor`  | `Color.Transparent`       |
| `DisabledAlpha`    | `0.38f`                   |


### `CompareLabelDefaults`


| Property          | Value                            |
| ----------------- | -------------------------------- |
| `BeforeText`      | `"Before"`                       |
| `AfterText`       | `"After"`                        |
| `Padding`         | `8.dp`                           |
| `BackgroundColor` | `Color.Black` @ 60% alpha        |
| `TextColor`       | `Color.White`                    |
| `CornerRadius`    | `4.dp`                           |
| `TextStyle`       | Material 3 `labelSmall`–inspired |


---

## Screenshots


| Basic | Customized | Labels |
| ----- | ---------- | ------ |
|       |            |        |



| Gestures | Live Controls | Sample App |
| -------- | ------------- | ---------- |
|          |               |            |


> Add your screenshots to `docs/images/` and they will render automatically.

---

## Platform Support


| Platform      | Status      | Target              |
| ------------- | ----------- | ------------------- |
| Android       | ✅ Supported | `minSdk 24`         |
| iOS Device    | ✅ Supported | `iosArm64`          |
| iOS Simulator | ✅ Supported | `iosSimulatorArm64` |
| Desktop       | 🔜 Planned  | —                   |
| Web           | 🔜 Planned  | —                   |


---

## API Overview


| API                            | Description                                   |
| ------------------------------ | --------------------------------------------- |
| `BeforeAfterSlider`            | Main before/after comparison composable       |
| `SliderState`                  | Hoisted state holding slider progress         |
| `rememberCompareSliderState()` | Creates and remembers a `SliderState`         |
| `CompareKitDefaults`           | Default slider appearance and behavior        |
| `CompareLabel`                 | Styled label composable for before/after text |
| `CompareLabelDefaults`         | Default label styling values                  |
| `CompareLabelAlignment`        | Label placement options                       |
| `SliderDefaults`               | Deprecated — use `CompareKitDefaults`         |


**Package:** `com.usmandev.comparekit.beforeafterslider`  
**Maven:** `io.github.usmandev0:comparekit`

---

## Changelog

See [CHANGELOG.md](CHANGELOG.md) for the full release history.

**v0.1.2** (latest)

- Vertical slider — `SliderOrientation.Vertical` for top/bottom comparison
- Coil image loading — `CompareImageSource` with URL, file, drawable, and bitmap support
- Thumb icon and vertical label positioning fixes

---

## Sample App

The repository includes a full interactive demo in the `androidApp` module.

```bash
git clone https://github.com/usmandev0/comparekit.git
cd comparekit
./gradlew :androidApp:installDebug
```

The sample demonstrates:

1. **Basic Slider** — default configuration
2. **Coil & Custom Thumb** — remote images and thumb icons
3. **Customization** — divider, thumb, corners, and visibility
4. **Labels** — default, custom, hidden, and alignment options
5. **Gestures** — drag, double-tap reset, and long press
6. **Orientation** — horizontal and vertical sliders
7. **Live Controls** — shared controls that update all sliders

---

## Performance

CompareKit is designed to be lightweight and recomposes efficiently.

- **Stable state** — `SliderState` is annotated `@Stable` for smart recomposition
- **Remembered conversions** — dp-to-px and shape calculations are cached
- **Minimal dependencies** — Compose Runtime, Foundation, Animation, and UI only
- **No overdraw tricks** — images are clipped with efficient `drawWithContent` scopes
- **Gesture isolation** — label overlays use pointer passthrough so dragging stays smooth

---

## Why CompareKit?

**Built for Kotlin Multiplatform.** One composable, every platform — no `expect`/`actual` UI bridges.

**Compose-native.** CompareKit is not a WebView wrapper or a platform view interop layer. It is pure Compose Multiplatform drawing and gesture handling.

**Customizable without complexity.** Sensible defaults mean zero configuration works beautifully. Every knob is available when you need it.

**Production-minded API.** Hoisted state, gesture callbacks, animated double-tap reset, and KDoc on every public entry point.

**Honest scope.** CompareKit does one thing well — before/after image comparison — without pulling in heavy dependencies.

---

## Roadmap


| Feature                                 | Status      |
| --------------------------------------- | ----------- |
| Horizontal slider                       | ✅ Available |
| Customization API                       | ✅ Available |
| Labels                                  | ✅ Available |
| Gestures (drag, double-tap, long press) | ✅ Available |
| Vertical slider                         | ✅ Available |
| Zoom                                    | 🔜 Planned  |
| Magnifier                               | 🔜 Planned  |
| Video comparison                        | 🔜 Planned  |
| Desktop (macOS, Windows, Linux)         | 🔜 Planned  |
| Web (Wasm)                              | 🔜 Planned  |
| Accessibility improvements              | 🔜 Planned  |


Track progress and propose features on [GitHub Issues](https://github.com/usmandev0/comparekit/issues).

---

## Contributing

Contributions are welcome and appreciated.

1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feature/my-feature`)
3. **Commit** your changes (`git commit -m "Add my feature"`)
4. **Push** to the branch (`git push origin feature/my-feature`)
5. **Open** a Pull Request

### Development

```bash
# Build Android
./gradlew :CompareKit:compileAndroidMain

# Build iOS Simulator
./gradlew :CompareKit:compileKotlinIosSimulatorArm64

# Run sample app
./gradlew :sample:installDebug
```

Please follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html) and keep changes focused.

---

## License

```
Copyright 2026 Usman

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

---

## Author

**Usman**

- GitHub: [@usmandev0](https://github.com/usmandev0)
- Repository: [github.com/usmandev0/comparekit](https://github.com/usmandev0/comparekit)

---

## Support

If CompareKit helps your project, consider:

- ⭐ **Starring** the repository on [GitHub](https://github.com/usmandev0/comparekit)
- 🐛 **Reporting bugs** via [GitHub Issues](https://github.com/usmandev0/comparekit/issues)
- 💡 **Requesting features** via [GitHub Discussions](https://github.com/usmandev0/comparekit/discussions)
- 📦 **Sharing** CompareKit with your team

---

### Star History

[Star History Chart](https://star-history.com/#usmandev0/comparekit&Date)

**Made with ❤️ using Kotlin Multiplatform and Compose Multiplatform**
