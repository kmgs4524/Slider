package com.york.slider.gesture

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.york.slider.Step
import com.york.slider.component.ThumbType

interface ThumbState {
    val leftThumbStep: Step
    val rightThumbStep: Step

    val leftThumbOffset: Float
    val rightThumbOffset: Float

    @Composable
    fun provideThumbModifierMap(): Map<ThumbType, Modifier>
}