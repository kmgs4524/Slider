package com.york.slider.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp

@Composable
fun Track(
    modifier: Modifier = Modifier,
    activeTrackColor: Color,
    inactiveTrackColor: Color,
    trackStrokeWidth: Dp,
    stepOffsetMap: Map<Int, Int>,
    leftThumbOffset: Float,
    rightThumbOffset: Float,
    stepIndicatorActiveColor: Color,
    stepIndicatorInactiveColor: Color
) {
    Canvas(
        modifier = modifier
            .height(trackStrokeWidth)
            .fillMaxWidth()
    ) {
        val canvasWidth = size.width

        // Inactive track
        drawLine(
            color = inactiveTrackColor,
            strokeWidth = trackStrokeWidth.toPx(),
            start = Offset(x = 12f, 0f),
            end = Offset(x = canvasWidth - 12, y = 0f),
            cap = StrokeCap.Round
        )

        // Active track
        drawLine(
            color = activeTrackColor,
            strokeWidth = trackStrokeWidth.toPx(),
            start = Offset(x = leftThumbOffset, 0f),
            end = Offset(x = rightThumbOffset, y = 0f),
            cap = StrokeCap.Round
        )

        // Step indicator
        stepOffsetMap.forEach { (key, value) ->
            // 最左邊跟最右邊不用畫圓點
            if (key in 1 until stepOffsetMap.size - 1) {
                val indicatorOffset = stepOffsetMap[key]?.toFloat() ?: 0f
                val color =
                    if (indicatorOffset in leftThumbOffset..rightThumbOffset) {
                        stepIndicatorActiveColor
                    } else {
                        stepIndicatorInactiveColor
                    }
                drawCircle(
                    color = color,
                    radius = 10f,
                    center = Offset(value.toFloat(), 0f)
                )
            }
        }
    }
}