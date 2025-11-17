package com.york.slider.gesture.draggable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.york.slider.Step
import com.york.slider.component.ThumbType
import com.york.slider.gesture.ThumbState

@Composable
fun rememberDraggableThumbState(
    gap: State<Int>,
    stepOffsetMap: Map<Step, Int>,
    thumbStepMap: Map<ThumbType, Step>
): ThumbState {
    val leftThumbStepState = remember(thumbStepMap) {
        mutableIntStateOf(thumbStepMap[ThumbType.LEFT] ?: -1)
    }
    val rightThumbStepState = remember {
        mutableIntStateOf(thumbStepMap[ThumbType.RIGHT] ?: -1)
    }

    return DraggableThumbState(
        gap = gap,
        stepOffsetMap = stepOffsetMap,
        leftThumbStepState = leftThumbStepState,
        rightThumbStepState = rightThumbStepState
    )
}

class DraggableThumbState(
    private val gap: State<Int>,
    private val stepOffsetMap: Map<Step, Int>,
    private val leftThumbStepState: MutableState<Step>,
    private val rightThumbStepState: MutableState<Step>
) : ThumbState {
    override val leftThumbStep: Int
        get() = leftThumbStepState.value
    override val rightThumbStep: Int
        get() = rightThumbStepState.value

    override val leftThumbOffset: Float
        get() = stepOffsetMap[leftThumbStepState.value]?.toFloat() ?: 0f
    override val rightThumbOffset: Float
        get() = stepOffsetMap[rightThumbStepState.value]?.toFloat() ?: 0f

    @Composable
    override fun provideThumbModifierMap(): Map<ThumbType, Modifier> {
        return mapOf(
            ThumbType.LEFT to Modifier.rangeDrag(
                thumbType = ThumbType.LEFT,
                gap = gap,
                stepOffsetMap = stepOffsetMap,
                thumbLeftStepState = leftThumbStepState,
                thumbRightStepState = rightThumbStepState
            ),
            ThumbType.RIGHT to Modifier.rangeDrag(
                thumbType = ThumbType.RIGHT,
                gap = gap,
                stepOffsetMap = stepOffsetMap,
                thumbLeftStepState = leftThumbStepState,
                thumbRightStepState = rightThumbStepState
            )
        )
    }
}