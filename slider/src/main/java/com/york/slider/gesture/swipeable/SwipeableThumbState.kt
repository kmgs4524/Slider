@file:OptIn(ExperimentalMaterialApi::class)

package com.york.slider.gesture.swipeable

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeableState
import androidx.compose.material.rememberSwipeableState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.york.slider.Step
import com.york.slider.component.ThumbType
import com.york.slider.gesture.ThumbState

@Composable
fun rememberSwipeableThumbState(
    stepOffsetMap: State<Map<Step, Int>>,
    thumbStepMap: Map<ThumbType, Step>
): ThumbState {
    val leftInitStepState = remember(thumbStepMap[ThumbType.LEFT]) {
        mutableIntStateOf(thumbStepMap[ThumbType.LEFT] ?: -1)
    }
    val rightInitStepState = remember(thumbStepMap[ThumbType.RIGHT]) {
        mutableIntStateOf(thumbStepMap[ThumbType.RIGHT] ?: -1)
    }
    val leftSwipeableState = rememberSwipeableState(leftInitStepState.intValue)
    val rightSwipeableState = rememberSwipeableState(rightInitStepState.intValue)

    // Reset the thumb's initial step should trigger the swipeable to snap to the new step
    // Because the rememberSwipeableState() not update state even if the initialValue is changed
    LaunchedEffect(key1 = leftInitStepState, key2 = rightInitStepState) {
        leftSwipeableState.snapTo(leftInitStepState.intValue)
        rightSwipeableState.snapTo(rightInitStepState.intValue)
    }

    return SwipeableThumbState(
        stepOffsetMap = stepOffsetMap,
        leftSwipeableState = leftSwipeableState,
        rightSwipeableState = rightSwipeableState
    )
}

class SwipeableThumbState(
    private val stepOffsetMap: State<Map<Step, Int>>,
    private val leftSwipeableState: SwipeableState<Step>,
    private val rightSwipeableState: SwipeableState<Step>
) : ThumbState {

    override val leftThumbStep: Step
        get() = leftSwipeableState.currentValue
    override val rightThumbStep: Step
        get() = rightSwipeableState.currentValue

    override val leftThumbOffset: Float
        get() = leftSwipeableState.offset.value
    override val rightThumbOffset: Float
        get() = rightSwipeableState.offset.value

    @Composable
    override fun provideThumbModifierMap(): Map<ThumbType, Modifier> {
        // Prevent error: "at least one anchor"
        if (stepOffsetMap.value.isEmpty()) return emptyMap()

        val zIndexMap = provideZIndexMap(
            stepOffsetMap = stepOffsetMap.value,
            leftSwipeableState = leftSwipeableState,
            rightSwipeableState = rightSwipeableState
        )

        return buildMap {
            put(
                ThumbType.LEFT, Modifier.rangeSwipeable(
                    thumbType = ThumbType.LEFT,
                    stepOffsetMap = stepOffsetMap.value,
                    leftSwipeableState = leftSwipeableState,
                    rightSwipeableState = rightSwipeableState,
                    zIndex = zIndexMap[ThumbType.LEFT]
                )
            )
            put(
                ThumbType.RIGHT, Modifier.rangeSwipeable(
                    thumbType = ThumbType.RIGHT,
                    stepOffsetMap = stepOffsetMap.value,
                    leftSwipeableState = leftSwipeableState,
                    rightSwipeableState = rightSwipeableState,
                    zIndex = zIndexMap[ThumbType.RIGHT]
                )
            )
        }
    }
}