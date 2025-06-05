package com.rizvee.colorbloom.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text // New import
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color // New import for combo text color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rizvee.colorbloom.game.Square


@Composable
import androidx.compose.foundation.background // New import
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box // New import
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight // New import
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rizvee.colorbloom.game.Square


@Composable
fun GameScreen(
    gridState: State<List<List<Square>>>,
    scoreState: State<Int>,
    comboMultiplierState: State<Int>,
    livesState: State<Int>,
    isGameOverState: State<Boolean>, // New parameter
    onSquareTap: (squareId: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) { // Root Box for potential overlay
        Column(
            modifier = Modifier.fillMaxSize(), // Existing main Column, padding moved to individual elements or sections
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Score Text
            Text(
                text = "Score: ${scoreState.value}",
                fontSize = 24.sp,
                modifier = Modifier.padding(top = 16.dp, bottom = 4.dp) // Removed .align from here
            )
            // Combo Text (if combo > 1)
            if (comboMultiplierState.value > 1) {
                Text(
                    text = "Combo: x${comboMultiplierState.value}",
                    fontSize = 20.sp,
                    color = Color.Magenta,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
            // Lives Text
            Text(
                text = "Lives: ${livesState.value}",
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            // BloomingGrid
            BloomingGrid(
                gridState = gridState,
                onSquareTap = onSquareTap,
                modifier = Modifier.padding(horizontal = 8.dp) // Grid specific padding
            )
        }

        if (isGameOverState.value) {
            Box( // Overlay Box
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.7f)) // Semi-transparent background
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Game Over!",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                // Restart button can be added here later
            }
        }
    }
}


@Composable
fun BloomingGrid(
    gridState: State<List<List<Square>>>,
    onSquareTap: (squareId: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val grid = gridState.value

    if (grid.isEmpty()) {
        return
    }

    // Changed modifier for BloomingGrid's Column to not fill max size from GameScreen directly,
    // but to be centered. GameScreen's Column handles overall fillMaxSize.
    Column(
        modifier = modifier, // Use passed modifier, e.g. padding from GameScreen
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        grid.forEach { rowOfSquares ->
            Row(
                // Removed fixed Arrangement.Center to allow SquareView's weight to work effectively
                // horizontalArrangement = Arrangement.SpaceEvenly, // Alternative for spacing
                modifier = Modifier.padding(vertical = 2.dp) // Add some vertical padding between rows
            ) {
                rowOfSquares.forEach { square ->
                    SquareView(
                        square = square,
                        onTap = { onSquareTap(square.id) },
                        modifier = Modifier.weight(1f) // Weight is crucial for equal distribution
                    )
                }
            }
        }
    }
}

@Composable
fun SquareView(
    square: Square,
    onTap: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier // This already includes .weight(1f) from BloomingGrid
            .aspectRatio(1f) // Keep squares as squares
            .padding(2.dp) // Spacing between squares (internal padding for the clickable area)
            .background(square.currentColor)
            .clickable { onTap() }
    ) {
        // Content inside the square
    }
}

// Previews would need to be updated to provide scoreState for GameScreen
// For example:
// @Preview(showBackground = true)
// @Composable
// fun GameScreenPreview() {
//     val previewGrid = remember { /* ... complex grid state ... */ }
//     val previewScore = remember { mutableStateOf(1000) }
//     ColorBloomTheme {
//         GameScreen(gridState = previewGrid, scoreState = previewScore, onSquareTap = {})
//     }
// }
