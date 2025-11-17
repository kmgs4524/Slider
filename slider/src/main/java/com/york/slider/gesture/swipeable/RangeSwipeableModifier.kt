@file:OptIn(ExperimentalMaterialApi::class)

package com.york.slider.gesture.swipeable

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.offset
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.zIndex
import com.york.slider.Step
import com.york.slider.component.ThumbType
import kotlin.math.roundToInt

/**
 * Modifier for range swipeable.
 *
 * @param thumbType The type of thumb. Default has two types: LEFT and RIGHT.
 * @param stepOffsetMap The map of step to offset.
 * @param leftSwipeableState The state of left swipeable.
 * @param rightSwipeableState The state of right swipeable.
 * @param zIndex The z-index of thumb.
 * @param leftSwipeOffset The offset of left swipeable.
 * @param rightSwipeOffset The offset of right swipeable.
 */
@Composable
fun Modifier.rangeSwipeable(
    thumbType: ThumbType,
    stepOffsetMap: Map<Step, Int>,
    leftSwipeableState: SwipeableState<Step>,
    rightSwipeableState: SwipeableState<Step>,
    zIndex: State<Float>?
): Modifier {
    val thumbModifier = Modifier
        .offset {
            if (thumbType == ThumbType.LEFT) {
                IntOffset(x = leftSwipeableState.offset.value.roundToInt(), 0)
            } else {
                IntOffset(x = rightSwipeableState.offset.value.roundToInt(), 0)
            }
        }
        .swipeable(
            state = if (thumbType == ThumbType.LEFT) {
                leftSwipeableState
            } else {
                rightSwipeableState
            },
            anchors = rememberThumbAnchorProvider(
                leftSwipeableState = leftSwipeableState,
                rightSwipeableState = rightSwipeableState,
                stepOffsetMap = stepOffsetMap,
                thumbType = thumbType
            ).anchors,
            thresholds = { _, _ -> FractionalThreshold(0.3f) },
            orientation = Orientation.Horizontal
        )
        .zIndex(zIndex?.value ?: 0f)
    return this then thumbModifier
}