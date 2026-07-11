package com.usmandev.comparekit.sample.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.usmandev.comparekit.beforeafterslider.BeforeAfterSlider
import com.usmandev.comparekit.beforeafterslider.CompareKitDefaults
import com.usmandev.comparekit.beforeafterslider.CompareLabel
import com.usmandev.comparekit.beforeafterslider.CompareLabelAlignment
import com.usmandev.comparekit.beforeafterslider.CompareLabelDefaults

/** Standard height used by demo sliders. */
internal val DemoSliderHeight = 220.dp

/**
 * Connected [BeforeAfterSlider] that reads appearance and behavior from [demoState].
 */
@Composable
fun ConnectedBeforeAfterSlider(
    demoState: DemoState,
    before: Painter,
    after: Painter,
    modifier: Modifier = Modifier,
    beforeLabel: (@Composable () -> Unit)? = null,
    afterLabel: (@Composable () -> Unit)? = null,
    showBeforeLabel: Boolean = demoState.showBeforeLabel,
    showAfterLabel: Boolean = demoState.showAfterLabel,
    labelAlignment: CompareLabelAlignment = demoState.labelAlignment,
    onDoubleTap: (() -> Unit)? = null,
    onLongPress: (() -> Unit)? = null,
) {
    val resolvedBeforeLabel = when {
        beforeLabel != null -> beforeLabel
        demoState.useCustomLabels -> {
            { CompareLabel(text = "Original", backgroundColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.85f)) }
        }
        else -> null
    }
    val resolvedAfterLabel = when {
        afterLabel != null -> afterLabel
        demoState.useCustomLabels -> {
            {
                Text(
                    text = "Edited",
                    style = MaterialTheme.typography.labelSmall,
                    color = CompareLabelDefaults.TextColor,
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.9f),
                            shape = RoundedCornerShape(CompareLabelDefaults.CornerRadius),
                        )
                        .padding(CompareLabelDefaults.ContentPadding),
                )
            }
        }
        else -> null
    }

    BeforeAfterSlider(
        before = before,
        after = after,
        modifier = modifier,
        state = demoState.sliderState,
        dividerColor = demoState.dividerColor,
        dividerThickness = demoState.dividerThickness,
        thumbColor = demoState.thumbColor,
        thumbSize = demoState.thumbSize,
        cornerRadius = demoState.cornerRadius,
        showDivider = demoState.showDivider,
        showThumb = demoState.showThumb,
        enabled = demoState.enabled,
        defaultSliderPosition = CompareKitDefaults.SliderPosition,
        beforeLabel = resolvedBeforeLabel,
        afterLabel = resolvedAfterLabel,
        showBeforeLabel = showBeforeLabel,
        showAfterLabel = showAfterLabel,
        labelAlignment = labelAlignment,
        onDoubleTap = onDoubleTap,
        onLongPress = onLongPress,
    )
}

/**
 * Controls used in the customization and live-controls sections.
 */
@Composable
fun CustomizationControls(
    demoState: DemoState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        ColorOptionRow(
            label = "Divider color",
            options = DemoDividerColorOptions,
            selectedColor = demoState.dividerColor,
            onColorSelected = { demoState.dividerColor = it },
        )
        DpSliderControl(
            label = "Divider thickness",
            value = demoState.dividerThickness,
            valueRange = DemoDividerThicknessRange,
            onValueChange = { demoState.dividerThickness = it },
        )
        ColorOptionRow(
            label = "Thumb color",
            options = DemoThumbColorOptions,
            selectedColor = demoState.thumbColor,
            onColorSelected = { demoState.thumbColor = it },
        )
        DpSliderControl(
            label = "Thumb size",
            value = demoState.thumbSize,
            valueRange = DemoThumbSizeRange,
            onValueChange = { demoState.thumbSize = it },
        )
        DpSliderControl(
            label = "Corner radius",
            value = demoState.cornerRadius,
            valueRange = DemoCornerRadiusRange,
            onValueChange = { demoState.cornerRadius = it },
        )
        SwitchControl(
            label = "Show divider",
            checked = demoState.showDivider,
            onCheckedChange = { demoState.showDivider = it },
        )
        SwitchControl(
            label = "Show thumb",
            checked = demoState.showThumb,
            onCheckedChange = { demoState.showThumb = it },
        )
        SwitchControl(
            label = "Enabled",
            checked = demoState.enabled,
            onCheckedChange = { demoState.enabled = it },
        )
    }
}

/**
 * Global live controls that update every connected demo slider at once.
 */
@Composable
fun LiveControlsPanel(
    demoState: DemoState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        FloatSliderControl(
            label = "Slider position",
            value = demoState.sliderState.progress,
            valueRange = DemoSliderPositionRange,
            valueLabel = "${demoState.sliderPositionPercent}%",
            onValueChange = demoState.sliderState::updateProgress,
        )
        DpSliderControl(
            label = "Divider width",
            value = demoState.dividerThickness,
            valueRange = DemoDividerThicknessRange,
            onValueChange = { demoState.dividerThickness = it },
        )
        DpSliderControl(
            label = "Thumb size",
            value = demoState.thumbSize,
            valueRange = DemoThumbSizeRange,
            onValueChange = { demoState.thumbSize = it },
        )
        DpSliderControl(
            label = "Corner radius",
            value = demoState.cornerRadius,
            valueRange = DemoCornerRadiusRange,
            onValueChange = { demoState.cornerRadius = it },
        )
        SwitchControl(
            label = "Enabled",
            checked = demoState.enabled,
            onCheckedChange = { demoState.enabled = it },
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            OutlinedButton(
                onClick = demoState::resetPosition,
                modifier = Modifier.weight(1f),
            ) {
                Text("Reset position")
            }
            Button(
                onClick = demoState::resetAll,
                modifier = Modifier.weight(1f),
            ) {
                Text("Reset all")
            }
        }
    }
}

/**
 * Label-specific controls for the labels demo section.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LabelControls(
    demoState: DemoState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        SwitchControl(
            label = "Show before label",
            checked = demoState.showBeforeLabel,
            onCheckedChange = { demoState.showBeforeLabel = it },
        )
        SwitchControl(
            label = "Show after label",
            checked = demoState.showAfterLabel,
            onCheckedChange = { demoState.showAfterLabel = it },
        )
        SwitchControl(
            label = "Use custom labels",
            checked = demoState.useCustomLabels,
            onCheckedChange = { demoState.useCustomLabels = it },
        )
        Text(
            text = "Label alignment",
            style = MaterialTheme.typography.labelLarge,
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            DemoLabelAlignmentOptions.forEach { alignment ->
                FilterChip(
                    selected = demoState.labelAlignment == alignment,
                    onClick = { demoState.labelAlignment = alignment },
                    label = { Text(alignment.name) },
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ColorOptionRow(
    label: String,
    options: List<Pair<String, Color>>,
    selectedColor: Color,
    onColorSelected: (Color) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            options.forEach { (name, color) ->
                FilterChip(
                    selected = selectedColor == color,
                    onClick = { onColorSelected(color) },
                    label = { Text(name) },
                    leadingIcon = {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(color),
                        )
                    },
                )
            }
        }
    }
}

@Composable
private fun DpSliderControl(
    label: String,
    value: Dp,
    valueRange: ClosedRange<Dp>,
    onValueChange: (Dp) -> Unit,
    modifier: Modifier = Modifier,
) {
    FloatSliderControl(
        label = label,
        value = value.value,
        valueRange = valueRange.start.value..valueRange.endInclusive.value,
        valueLabel = "${value.value.toInt()} dp",
        onValueChange = { onValueChange(it.dp) },
        modifier = modifier,
    )
}

@Composable
private fun FloatSliderControl(
    label: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    valueLabel: String,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = label, style = MaterialTheme.typography.labelLarge)
            Text(
                text = valueLabel,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
        )
    }
}

@Composable
private fun SwitchControl(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
