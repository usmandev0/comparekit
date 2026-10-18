<p align="center">
  <img src="images/comparekit-logo.png" alt="CompareKit logo" width="320" />
</p>

# Changelog

All notable changes to CompareKit are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [0.1.4] - 2026-10-18

### Added

- **Synced zoom and pan** — pinch, two-finger pan, and scroll-wheel zoom both images together via `CompareZoomState` / `rememberCompareZoomState()`. The divider still clips in viewport space so alignment is preserved. One-finger drag moves the slider at 1x, or when the gesture starts near the divider while zoomed; otherwise it pans. Double-tap resets zoom first, then slider position.
- **Zoom defaults** — `zoomEnabled` (on by default), `CompareKitDefaults.MinZoomScale`, and `CompareKitDefaults.MaxZoomScale`.
- **Accessibility** — `contentDescription`, `beforeContentDescription`, `afterContentDescription`, and `progressStep` on `BeforeAfterSlider`, with progress semantics (range, `setProgress`, `stateDescription`) and keyboard navigation (arrows, Home, End).
- **Accessibility defaults** — `CompareKitDefaults.ContentDescription` and `CompareKitDefaults.ProgressStep`.

### Changed

- Reorganized library internals into `defaults/` and `internal/` packages. Public imports stay the same via root typealiases for `CompareKitDefaults` and `CompareLabelDefaults`.

## [0.1.3] - 2026-08-03

### Added

- **Auto-scrub animation** — `CompareAnimationMode` with `PingPong` (0→1→0 loop) and `RevealOnce` (one-shot reveal) modes on `BeforeAfterSlider`.
- **Animation parameters** — `animationMode`, `autoScrubDurationMillis`, and `pauseAutoScrubOnInteraction` with defaults in `CompareKitDefaults`.
- **SliderState.animateToProgress()** — suspend helper for programmatic animated progress updates.
- **Demo app** — section 8 with mode, speed, and pause-on-drag controls.

### Changed

- Extended `sliderGestures` with `onInteractionStart` / `onInteractionEnd` callbacks to pause auto-scrub during user drag.
- Updated README with auto animation documentation and roadmap status.

## [0.1.2] - 2026-07-13

### Added

- **Vertical slider** — `SliderOrientation.Vertical` for top/bottom before/after comparison with orientation-aware clipping, divider, thumb placement, gestures, and labels.
- **Coil image loading** — `CompareImageSource` API with `painter`, `model`, and `url` helpers, plus `rememberCompareImagePainter` on Android and iOS.
- **CompareImageViewer** — shared image display composable used by `BeforeAfterSlider`.
- **SliderLayout helpers** — internal orientation math for clip bounds, divider offset, and thumb offset.
- **Demo app** — vertical orientation example in section 6; compare-arrows thumb icon for the demo.

### Changed

- Renamed the demo module from `sample` to `androidApp`.
- Consolidated slider defaults into `CompareKitDefaults` (removed `SliderDefaults`).
- Improved thumb icon rendering with centered alignment and reduced default icon padding (`6.dp` → `4.dp`).
- Fixed vertical label placement so `TopStart` places labels at the top and bottom edges instead of near the divider.
- Updated README with vertical slider documentation and roadmap status.

### Fixed

- Resolved `ComposePainter` reference in `CompareImageSource` by aliasing Compose's `Painter` type.
- Fixed thumb icon visibility on white thumbs in the demo (dark arrow drawable).
- Fixed thumb icon vertical centering inside the circular handle.

## [0.1.1] - 2026-07-11

### Added

- **Customization API** — divider color/thickness, thumb size/shape/color/border, corner radius, and show/hide toggles via `CompareKitDefaults`.
- **Labels** — `CompareLabel`, `CompareLabelAlignment`, and `CompareLabelDefaults` with six alignment options and custom composable support.
- **Gestures** — double-tap reset with spring animation, long-press callback, and enabled/disabled state.
- **Demo app** — interactive showcase with customization controls, label examples, gesture feedback, and live shared controls.
- Expanded README with full API documentation and usage examples.

### Changed

- Extended `BeforeAfterSlider` with label overlay, gesture handling, and richer parameter defaults.
- Replaced inline gesture code with `GestureHandler` and `sliderGestures` modifier.
- Improved thumb rendering to support custom painter icons and composable content.

## [0.1.0] - 2026-07-10

### Added

- Initial **BeforeAfterSlider** Compose Multiplatform library for horizontal before/after image comparison.
- **SliderState** and `rememberCompareSliderState` for hoisted progress control.
- Draggable divider and thumb with configurable appearance.
- Android sample app demonstrating the basic slider.
- Maven Central publishing setup and GitHub Actions CI/publish workflows.

### Fixed

- Fixed before/after image background bleed at clip boundaries.

[0.1.4]: https://github.com/usmandev0/comparekit/compare/v0.1.3...v0.1.4
[0.1.3]: https://github.com/usmandev0/comparekit/compare/v0.1.2...v0.1.3
[0.1.2]: https://github.com/usmandev0/comparekit/compare/v0.1.1...v0.1.2
[0.1.1]: https://github.com/usmandev0/comparekit/compare/v0.1.0...v0.1.1
[0.1.0]: https://github.com/usmandev0/comparekit/releases/tag/v0.1.0
