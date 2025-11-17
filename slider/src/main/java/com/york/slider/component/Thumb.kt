package com.york.slider.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.york.slider.theme.Colors

@Preview
@Composable
fun ThumbPreview(modifier: Modifier = Modifier) {
    Thumb(modifier = modifier.size(20.dp).border(1.dp, Colors.gray40, CircleShape))
}

@Composable
fun Thumb(modifier: Modifier, color: Color = Colors.gray10) {
    Canvas(modifier = modifier) {
        drawCircle(
            color = color,
            center = Offset(x = size.width / 2, y = size.height / 2),
        )
    }
}

enum class ThumbType {
    LEFT,
    RIGHT
}