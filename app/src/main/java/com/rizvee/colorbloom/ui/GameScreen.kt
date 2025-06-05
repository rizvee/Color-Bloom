package com.rizvee.colorbloom.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
//import androidx.compose.runtime.mutableStateOf // For preview
//import androidx.compose.runtime.remember // For preview
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color // For preview
//import androidx.compose.ui.tooling.preview.Preview // For preview
import androidx.compose.ui.unit.dp
import com.rizvee.colorbloom.game.Square
//import com.rizvee.colorbloom.ui.theme.ColorBloomTheme // For preview

@Composable
fun BloomingGrid(
    gridState: State<List<List<Square>>>,
    modifier: Modifier = Modifier
) {
    val grid = gridState.value // Access the list of squares

    if (grid.isEmpty()) {
        // Handle empty grid state, maybe show a loading indicator or default message
        // For now, just return to avoid crashing.
        return
    }

    Column(
        modifier = modifier.fillMaxSize().padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        grid.forEach { rowOfSquares ->
            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                rowOfSquares.forEach { square ->
                    // Pass the weight modifier here to ensure equal sizing within the Row
                    SquareView(square = square, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun SquareView(
    square: Square,
    modifier: Modifier = Modifier // Modifier passed from the caller, including weight
) {
    Box(
        modifier = modifier // Use the modifier passed, which includes .weight(1f)
            .padding(2.dp) // Spacing between squares
            .aspectRatio(1f) // Keep squares as squares
            .background(square.currentColor)
            // .weight(1f) // This was moved to the caller to be applied to the SquareView itself within the Row
    ) {
        // Content inside the square, e.g., score indicators later
        // For now, it's just a colored box.
    }
}

// Preview for BloomingGrid (optional, but good for development)
// @Preview(showBackground = true)
// @Composable
// fun BloomingGridPreview() {
//     // This preview would need a mock GameViewModel or a sample grid state
//     // For simplicity, we'll skip the complex preview setup in this subtask,
//     // but it's good practice for actual development.
//     val previewGrid = remember {
//         mutableStateOf(
//             listOf(
//                 listOf(Square(0,0,0, initialColor = Color.Red), Square(1,0,1, initialColor = Color.Green), Square(2,0,2, initialColor = Color.Blue)),
//                 listOf(Square(3,1,0, initialColor = Color.Yellow), Square(4,1,1, initialColor = Color.Cyan), Square(5,1,2, initialColor = Color.Magenta)),
//                 listOf(Square(6,2,0, initialColor = Color.Gray), Square(7,2,1, initialColor = Color.Black), Square(8,2,2, initialColor = Color.LightGray))
//             )
//         )
//     }
//    ColorBloomTheme {
//        BloomingGrid(gridState = previewGrid)
//    }
// }
