@file:OptIn(ExperimentalMaterialApi::class)

package com.york.slider.gesture.swipeable

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeableState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.york.slider.Step
import com.york.slider.component.ThumbType

/**
 * Remember the thumb anchor provider. Each step maps to an anchor that thumb can snap to.
 */
@Composable
internal fun rememberThumbAnchorProvider(
    leftSwipeableState: SwipeableState<Step>,
    rightSwipeableState: SwipeableState<Step>,
    stepOffsetMap: Map<Step, Int>,
    thumbType: ThumbType
): ThumbAnchorProvider<Step> {
    val anchorProvider by remember(leftSwipeableState, rightSwipeableState, stepOffsetMap) {
        derivedStateOf {
            ThumbAnchorProviderImpl(
                leftSwipeableState = leftSwipeableState,
                rightSwipeableState = rightSwipeableState,
                stepOffsetMap = stepOffsetMap,
                thumbType = thumbType
            )
        }
    }
    return anchorProvider
}

interface ThumbAnchorProvider <AnchorType> {
    val anchors: Map<Float, AnchorType>
}

internal class ThumbAnchorProviderImpl(
    private val leftSwipeableState: SwipeableState<Step>,
    private val rightSwipeableState: SwipeableState<Step>,
    private val stepOffsetMap: Map<Step, Int>,
    private val thumbType: ThumbType
) : ThumbAnchorProvider<Step> {

    override val anchors: Map<Float, Step>
        get() = buildMap {
            if (thumbType == ThumbType.LEFT) {
                stepOffsetMap.filter { it.key <= rightSwipeableState.currentValue }
                    .forEach { (step, offset) ->
                        put(offset.toFloat(), step)
                    }
            } else if (thumbType == ThumbType.RIGHT) {
                stepOffsetMap.filter { it.key >= leftSwipeableState.currentValue }
                    .forEach { (step, offset) ->
                        put(offset.toFloat(), step)
                    }
            }
        }
}