# Slider Compose Component

A customizable range slider component for Jetpack Compose that allows users to select a range of values with dual thumbs.

## Features

- **Range Selection**: Dual thumb slider for selecting a value range
- **Step-based Navigation**: Snap to discrete steps with customizable intervals
- **Customizable Labels**: Display custom labels at each step position
- **Visual Indicators**: Step indicators that reflect active/inactive states
- **Flexible Theming**: Customize colors, sizes, and styles for all components
- **Gesture Support**: Smooth swipeable and draggable thumb interactions
- **Compose-first**: Built entirely with Jetpack Compose

## Requirements

- Minimum SDK: 24
- Target SDK: 35
- Jetpack Compose

## Installation

Add the slider module to your project:

```kotlin
dependencies {
    implementation(project(":slider"))
}
```

## Usage

### Basic Example

```kotlin
EatigoRangeSlider(
    label = listOf("$", "$$", "$$$", "$$$$", "$$$$$"),
    steps = 5,
    thumbStepMap = mapOf(
        ThumbType.LEFT to 0,
        ThumbType.RIGHT to 4
    ),
    onRangeChanged = { range ->
        // Handle range change
        println("Selected range: ${range.first} to ${range.last}")
    }
)
```

### Advanced Customization

```kotlin
EatigoRangeSlider(
    modifier = Modifier
        .height(96.dp)
        .background(Color.White)
        .padding(horizontal = 42.dp),
    label = listOf("Min", "Low", "Med", "High", "Max"),
    labelOffsetX = 20.dp,
    activeTrackColor = Color.Red,
    inactiveTrackColor = Color.Gray,
    trackStrokeWidth = 8.dp,
    stepIndicatorActiveColor = Color.DarkRed,
    stepIndicatorInactiveColor = Color.LightGray,
    thumbs = { modifierMap ->
        Thumb(
            modifierMap
                .getOrDefault(ThumbType.LEFT, Modifier)
                .then(
                    Modifier
                        .size(24.dp)
                        .border(2.dp, Color.Red, CircleShape)
                )
        )
        Thumb(
            modifierMap
                .getOrDefault(ThumbType.RIGHT, Modifier)
                .then(
                    Modifier
                        .size(24.dp)
                        .border(2.dp, Color.Red, CircleShape)
                )
        )
    },
    steps = 5,
    thumbStepMap = mapOf(
        ThumbType.LEFT to 1,
        ThumbType.RIGHT to 3
    ),
    onRangeChanged = { range ->
        // Handle range change
    }
)
```

## API Reference

### EatigoRangeSlider Parameters

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `modifier` | `Modifier` | Default layout modifier | Modifier for the slider layout |
| `label` | `List<String>` | `["$", "$$", "$$$", "$$$$", "$$$$$"]` | Labels displayed at each step |
| `labelOffsetX` | `Dp` | `20.dp` | Vertical offset of labels from track |
| `trackModifier` | `Modifier` | `Modifier` | Modifier for the track component |
| `activeTrackColor` | `Color` | `Colors.red50` | Color of the active track section |
| `inactiveTrackColor` | `Color` | `Colors.gray30` | Color of the inactive track section |
| `trackStrokeWidth` | `Dp` | `8.dp` | Width of the track line |
| `stepIndicatorActiveColor` | `Color` | `Colors.red60` | Color of step indicators in active range |
| `stepIndicatorInactiveColor` | `Color` | `Colors.gray60` | Color of step indicators outside range |
| `thumbs` | `@Composable` | Default thumb implementation | Custom thumb composable function |
| `steps` | `Int` | `5` | Number of steps on the slider |
| `thumbStepMap` | `Map<ThumbType, Step>` | Left: 0, Right: 4 | Initial positions for left and right thumbs |
| `onRangeChanged` | `(IntRange) -> Unit` | `{}` | Callback when range selection changes |

## Component Structure

The slider consists of three main components:

1. **Labels**: Text labels positioned above each step
2. **Track**: The slider track with active/inactive sections and step indicators
3. **Thumbs**: Draggable thumb controls for range selection

## How It Works

- The slider divides the available width into equal steps
- Users can drag either thumb to select a range
- Thumbs snap to the nearest step position
- The track updates to show the active range visually
- Step indicators change color based on whether they're in the selected range
- The `onRangeChanged` callback fires when the selection changes

## Customization Examples

### Custom Thumb Design

```kotlin
thumbs = { modifierMap ->
    // Custom left thumb
    Thumb(
        modifierMap
            .getOrDefault(ThumbType.LEFT, Modifier)
            .then(Modifier.size(30.dp)),
        color = Color.Blue
    )
    // Custom right thumb
    Thumb(
        modifierMap
            .getOrDefault(ThumbType.RIGHT, Modifier)
            .then(Modifier.size(30.dp)),
        color = Color.Green
    )
}
```

### Price Range Example

```kotlin
EatigoRangeSlider(
    label = listOf("Free", "$10", "$20", "$30", "$40+"),
    steps = 5,
    onRangeChanged = { range ->
        val minPrice = range.first * 10
        val maxPrice = range.last * 10
        // Use minPrice and maxPrice
    }
)
```

## License

MIT License

Copyright (c) 2025 kmgs4524

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

## Contributing

This is a personal project and is not currently accepting contributions.