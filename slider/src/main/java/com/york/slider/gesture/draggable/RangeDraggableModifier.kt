package com.york.slider.gesture.draggable

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import com.york.slider.Step
import com.york.slider.component.ThumbType
import kotlin.math.abs

/**
 * Modifier for range draggable.
 *
 * @param thumbType The type of thumb. Default has two types: LEFT and RIGHT.
 * @param gap The gap between two thumbs.
 * @param stepOffsetMap The map of step to offset.
 * @param thumbLeftStepState The state of left thumb current step.
 * @param thumbRightStepState The state of right thumb current step.
 */
@Composable
fun Modifier.rangeDrag(
    thumbType: ThumbType,
    gap: State<Int>,
    stepOffsetMap: Map<Step, Int>,
    thumbLeftStepState: MutableState<Step>,
    thumbRightStepState: MutableState<Step>
): Modifier {
    val sumDragAmount = remember {
        mutableFloatStateOf(0f)
    }
    val thumbStepState = if (thumbType == ThumbType.LEFT) {
        thumbLeftStepState
    } else {
        thumbRightStepState
    }
    val otherThumbOffsetState = if (thumbType == ThumbType.LEFT) {
        thumbRightStepState
    } else {
        thumbLeftStepState
    }
    val draggableModifier = Modifier
        .offset {
            IntOffset(stepOffsetMap[thumbStepState.value] ?: 0, 0)
        }
        .pointerInput(Unit) {
            detectDragGestures(
                onDragEnd = { sumDragAmount.floatValue = 0f }
            ) { change, dragAmount ->
                change.consume()
                sumDragAmount.floatValue += dragAmount.x

                if (abs(sumDragAmount.floatValue) > (gap.value.toFloat() * 0.3)) {

                    val notExceedSides = thumbStepState.notExceedSides(
                        thumbType,
                        sumDragAmount.floatValue,
                        stepOffsetMap,
                        otherThumbOffsetState.value
                    )

                    if (notExceedSides) {
                        thumbStepState.value += if (sumDragAmount.floatValue > 0) 1 else -1
                        sumDragAmount.floatValue = 0f
                    }
                    if (shouldMoveOtherThumb(
                            thumbType = thumbType,
                            sumDragAmount = sumDragAmount.floatValue,
                            thumbStepState = thumbStepState,
                            otherThumbStepState = otherThumbOffsetState,
                            stepOffsetMap = stepOffsetMap
                        )
                    ) {
                        otherThumbOffsetState.value += if (sumDragAmount.floatValue > 0) 1 else -1
                        sumDragAmount.floatValue = 0f
                    }
                }
            }
        }
    return this then draggableModifier
}

/**
 * Check the thumb whether touch two sides: the other thumb's position and slider's left or right side.
 */
private fun State<Step>.notExceedSides(
    thumbType: ThumbType,
    sumDragAmount: Float,
    stepOffsetMap: Map<Step, Int>,
    otherThumbStep: Step
): Boolean {
    val rightSide = if (thumbType == ThumbType.RIGHT) {
        stepOffsetMap.keys.max()
    } else {
        otherThumbStep
    }
    val leftSide = if (thumbType == ThumbType.RIGHT) {
        otherThumbStep
    } else {
        stepOffsetMap.keys.min()
    }
    return sumDragAmount > 0 && this.value < rightSide ||
            sumDragAmount < 0 && this.value > leftSide
}

/**
 * The other thumb should move if it's touched by dragging thumb and not touch the slider's left or right side.
 */
private fun shouldMoveOtherThumb(
    thumbType: ThumbType,
    sumDragAmount: Float,
    thumbStepState: MutableState<Step>,
    otherThumbStepState: MutableState<Step>,
    stepOffsetMap: Map<Step, Int>
): Boolean {
    if (thumbStepState.value != otherThumbStepState.value) return false

    return if (thumbType == ThumbType.LEFT) {
        sumDragAmount > 0 && otherThumbStepState.value < stepOffsetMap.keys.max()
    } else {
        sumDragAmount < 0 && otherThumbStepState.value > stepOffsetMap.keys.min()
    }
}