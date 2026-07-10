# CompareKit

CompareKit is an open-source [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html) library for interactive before/after image comparison, built entirely with [Compose Multiplatform](https://www.jetbrains.com/compose-multiplatform/).

Package: `com.usmandev.comparekit`

Supported targets:
- Android
- iOS (`iosArm64`, `iosSimulatorArm64`)

## Features

- Horizontal before/after image comparison slider
- Smooth drag interaction with a circular thumb
- Full-height divider line
- 100% `commonMain` implementation — no platform-specific UI code
- Material-inspired defaults (white divider and thumb)

## Installation

Add the dependency to your `commonMain` source set:

```kotlin
// libs.versions.toml
[versions]
comparekit = "0.1.0"

[libraries]
comparekit = { module = "io.github.usmandev0:comparekit", version.ref = "comparekit" }
```

```kotlin
// build.gradle.kts
commonMain.dependencies {
    implementation(libs.comparekit)
}
```

Maven coordinates:

```text
io.github.usmandev0:comparekit:0.1.0
```

## Usage

Provide two `Painter` instances for the before and after images:

```kotlin
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.usmandev.comparekit.BeforeAfterSlider
import com.usmandev.comparekit.rememberSliderState

@Composable
fun ComparisonExample(
    beforePainter: Painter,
    afterPainter: Painter,
) {
    BeforeAfterSlider(
        before = beforePainter,
        after = afterPainter,
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp),
        state = rememberSliderState(initialProgress = 0.5f),
    )
}
```

The slider starts at **50%** by default. Users can drag horizontally to reveal the after image.

## Public API

### `BeforeAfterSlider`

```kotlin
@Composable
fun BeforeAfterSlider(
    before: Painter,
    after: Painter,
    modifier: Modifier = Modifier,
    state: SliderState = rememberSliderState(),
)
```

### `SliderState`

Holds slider progress in the range `0f..1f`:

- `0f` — only the before image is visible
- `1f` — the after image is fully revealed

```kotlin
val state = rememberSliderState(initialProgress = 0.5f)
state.updateProgress(0.75f)
```

### `SliderDefaults`

Default visual values:

| Property | Value |
|----------|-------|
| Divider thickness | `2.dp` |
| Thumb size | `32.dp` |
| Divider color | White |
| Thumb color | White |

## Requirements

- Kotlin `2.4.0+`
- Compose Multiplatform `1.11.1+`
- Android `minSdk 24+`

Your app must already use Compose Multiplatform for Android and/or iOS.

## Development

Clone the repository and build the library:

```bash
./gradlew :CompareKit:compileAndroidMain
./gradlew :CompareKit:compileKotlinIosSimulatorArm64
```

Project structure:

```text
CompareKit/src/commonMain/kotlin/com/usmandev/comparekit/
├── BeforeAfterSlider.kt
├── SliderState.kt
├── SliderDefaults.kt
├── SliderGesture.kt
├── Divider.kt
└── Thumb.kt
```

## CI and publishing

GitHub Actions workflows:

| Workflow | Trigger |
|----------|---------|
| **Release** | Push to `main` when `version` changes in `CompareKit/build.gradle.kts` |
| **Build** | Manual |
| **Publish** | Manual |

When the library version changes on `main`, the Release workflow builds Android and iOS artifacts and publishes to Maven Central.

Required GitHub secrets for publishing:

| Secret | Description |
|--------|-------------|
| `MAVEN_CENTRAL_USERNAME` | Sonatype Central Portal username |
| `MAVEN_CENTRAL_PASSWORD` | Sonatype Central Portal token |
| `SIGNING_KEY_ID` | GPG key ID (8, 16, or 40 hex characters) |
| `SIGNING_PASSWORD` | GPG key passphrase |
| `GPG_KEY_CONTENTS` | Armored private GPG key |

Get your GPG key ID:

```bash
gpg --list-secret-keys --keyid-format=long
```

Export the private key:

```bash
gpg --export-secret-keys --armor YOUR_KEY_ID
```

## License

```text
Copyright 2026 Usman

Licensed under the Apache License, Version 2.0.
See LICENSE for details.
```

## Links

- [Maven Central](https://central.sonatype.com/)
- [Compose Multiplatform](https://www.jetbrains.com/compose-multiplatform/)
- [Publishing guide](https://vanniktech.github.io/gradle-maven-publish-plugin/central/)
