@file:OptIn(ExperimentalMaterialApi::class)

package com.york.slider.gesture.swipeable

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeableState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import com.york.slider.Step
import com.york.slider.component.ThumbType

/**
 * Provide the z-index for the thumbs to handle the overlapping issue.
 * If swipeable thumb is overlapping to the other thumb, the z-index will be changed to 10 to make it on top.
 */
@Composable
fun provideZIndexMap(
    stepOffsetMap: Map<Step, Int>,
    leftSwipeableState: SwipeableState<Step>,
    rightSwipeableState: SwipeableState<Step>
): Map<ThumbType, State<Float>> {
    val leftZIndex = remember { mutableFloatStateOf(0f) }
    val rightZIndex = remember { mutableFloatStateOf(0f) }

    return ZIndexProviderImpl(stepOffsetMap, leftSwipeableState, rightSwipeableState, leftZIndex, rightZIndex).provideZIndexMap()
}

interface ZIndexProvider {

    @Composable
    fun provideZIndexMap(): Map<ThumbType, State<Float>>
}

class ZIndexProviderImpl(
    private val stepOffsetMap: Map<Step, Int>,
    private val leftSwipeableState: SwipeableState<Step>,
    private val rightSwipeableState: SwipeableState<Step>,
    private val leftZIndex: MutableFloatState,
    private val rightZIndex: MutableFloatState
) : ZIndexProvider {

    @Composable
    override fun provideZIndexMap(): Map<ThumbType, State<Float>> {
        // If left or right thumb is moving to the other thumb, it will be on top.
        // Target value equals to the current value when stop moving,
        // so we need to check 'target != current' to prevent the other thumb trigger zIndex changed.
        when {
            leftSwipeableState.targetValue != leftSwipeableState.currentValue && leftSwipeableState.targetValue == rightSwipeableState.currentValue -> {
                leftZIndex.floatValue = 10f
                rightZIndex.floatValue = 0f
            }
            rightSwipeableState.targetValue != rightSwipeableState.currentValue && rightSwipeableState.targetValue == leftSwipeableState.currentValue -> {
                rightZIndex.floatValue = 10f
                leftZIndex.floatValue = 0f
            }
            // When right thumb is at the slider's right side and overlap left thumb, must set left thumb on the top
            rightSwipeableState.currentValue == stepOffsetMap.maxByOrNull { it.key }?.key && leftSwipeableState.targetValue == rightSwipeableState.targetValue -> {
                rightZIndex.floatValue = 0f
                leftZIndex.floatValue = 10f
            }
        }
        return mutableMapOf(
            ThumbType.LEFT to leftZIndex,
            ThumbType.RIGHT to rightZIndex,
        )
    }
}