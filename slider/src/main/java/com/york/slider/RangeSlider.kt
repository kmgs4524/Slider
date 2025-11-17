package com.york.slider

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.york.slider.component.Thumb
import com.york.slider.component.ThumbType
import com.york.slider.component.Track
import com.york.slider.gesture.swipeable.rememberSwipeableThumbState
import com.york.slider.theme.Colors
import kotlin.math.roundToInt

val DEFAULT_SLIDER_MODIFIER = Modifier
    .height(96.dp)
    .background(Colors.gray10)
    .padding(horizontal = 42.dp)
val DEFAULT_THUMB_MODIFIER = Modifier
    .size(20.dp)
    .border(1.dp, Colors.gray40, CircleShape)

typealias Step = Int

private enum class Slot {
    LABEL,
    TRACK,
    THUMB
}

/**
 * EatigoSlider is a slider component that allows users to select a range of values.
 * Consists of three parts: label, track, and thumbs.
 *
 * @param modifier Modifier to be applied to the layout.
 * @param label List of labels to be displayed on the slider.
 * @param labelOffsetX Offset of the label from the top of the track.
 * @param trackModifier Modifier to be applied to the track.
 * @param activeTrackColor Color of the active track.
 * @param inactiveTrackColor Color of the inactive track.
 * @param trackStrokeWidth Width of the track.
 * @param thumbs Composable function to draw the thumbs.
 * @param steps Number of steps on the slider.
 * @param thumbStepMap Map of thumb type to step.
 * @param onRangeChanged Callback when the range is changed.
 */
@Preview(showBackground = true)
@Composable
fun EatigoRangeSlider(
    modifier: Modifier = DEFAULT_SLIDER_MODIFIER,
    label: List<String> = listOf("$", "$$", "$$$", "$$$$", "$$$$$"),
    labelOffsetX: Dp = 20.dp,
    trackModifier: Modifier = Modifier,
    // Track
    activeTrackColor: Color = Colors.red50,
    inactiveTrackColor: Color = Colors.gray30,
    trackStrokeWidth: Dp = 8.dp,
    stepIndicatorActiveColor: Color = Colors.red60,
    stepIndicatorInactiveColor: Color = Colors.gray60,
    // Thumbs
    thumbs: @Composable (Map<ThumbType, Modifier>) -> Unit = { map ->
        Thumb(
            map
                .getOrDefault(ThumbType.LEFT, Modifier)
                .then(DEFAULT_THUMB_MODIFIER)
        )
        Thumb(
            map
                .getOrDefault(ThumbType.RIGHT, Modifier)
                .then(DEFAULT_THUMB_MODIFIER)
        )
    },
    // Step
    steps: Int = 5,
    thumbStepMap: Map<ThumbType, Step> = mapOf(
        ThumbType.LEFT to 0,
        ThumbType.RIGHT to 4
    ),
    onRangeChanged: (IntRange) -> Unit = {}
) {
    val maxWidth = remember {
        mutableIntStateOf(0)
    }
    val gapBetweenSteps = remember(maxWidth) {
        derivedStateOf {
            maxWidth.intValue / (steps - 1)
        }
    }
    val stepOffsetMap: State<Map<Step, Int>> = remember(gapBetweenSteps) {
        derivedStateOf {
            buildMap {
                var currentOffset = 0
                for (i in 0 until steps) {
                    put(i, currentOffset)
                    currentOffset += gapBetweenSteps.value
                }
            }
        }
    }
    val thumbState = rememberSwipeableThumbState(stepOffsetMap, thumbStepMap)
    val onRangeChangedState by rememberUpdatedState(newValue = onRangeChanged)
    LaunchedEffect(key1 = Unit) {
        snapshotFlow { thumbState.leftThumbStep to thumbState.rightThumbStep }
            .collect { (leftThumbStep, rightThumbStep) ->
                onRangeChangedState(IntRange(leftThumbStep, rightThumbStep))
            }
    }

    SubcomposeLayout(modifier = modifier) { constraints ->
        maxWidth.intValue = constraints.maxWidth

        val labelPlaceable = subcompose(Slot.LABEL) {
            label.forEach {
                Text(text = it)
            }
        }.map {
            it.measure(
                constraints.copy(
                    minHeight = it.minIntrinsicHeight(constraints.maxWidth),
                    maxHeight = it.maxIntrinsicHeight(constraints.maxWidth)
                )
            )
        }

        val trackPlaceable = subcompose(Slot.TRACK) {
            Track(
                modifier = trackModifier,
                activeTrackColor = activeTrackColor,
                inactiveTrackColor = inactiveTrackColor,
                trackStrokeWidth = trackStrokeWidth,
                stepIndicatorActiveColor = stepIndicatorActiveColor,
                stepIndicatorInactiveColor = stepIndicatorInactiveColor,
                stepOffsetMap = stepOffsetMap.value,
                leftThumbOffset = thumbState.leftThumbOffset,
                rightThumbOffset = thumbState.rightThumbOffset
            )
        }.map {
            it.measure(constraints)
        }

        val thumbPlaceable = subcompose(Slot.THUMB) {
            thumbs(thumbState.provideThumbModifierMap())
        }.map {
            it.measure(
                constraints.copy(
                    minHeight = it.minIntrinsicHeight(constraints.minWidth),
                    maxHeight = it.maxIntrinsicHeight(constraints.minWidth)
                )
            )
        }

        // Layout placeable to relative position
        layout(constraints.maxWidth, constraints.maxHeight) {
            labelPlaceable.forEachIndexed { index, placeable ->
                val stepLabelOffset = stepOffsetMap.value[index] ?: 0
                placeable.place(
                    x = stepLabelOffset - placeable.width / 2,
                    y = (constraints.maxHeight * 0.6).roundToInt() - trackStrokeWidth.roundToPx() - placeable.height - labelOffsetX.roundToPx()
                )
            }
            trackPlaceable.forEach {
                it.place(0, (constraints.maxHeight * 0.6).roundToInt())
            }
            thumbPlaceable.forEachIndexed { _, placeable ->
                placeable.place(
                    0 - placeable.width / 2,
                    (constraints.maxHeight * 0.6).roundToInt() - (placeable.height / 2)
                )
            }
        }
    }
}