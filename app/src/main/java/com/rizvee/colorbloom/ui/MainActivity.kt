package com.rizvee.colorbloom.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
// import androidx.compose.material3.Text // No longer needed for Greeting
// import androidx.compose.runtime.Composable // No longer needed for Greeting
import androidx.compose.ui.Modifier
// import androidx.compose.ui.tooling.preview.Preview // No longer needed for GreetingPreview
import com.rizvee.colorbloom.ui.theme.ColorBloomTheme
import com.rizvee.colorbloom.game.GameViewModel
import androidx.lifecycle.viewmodel.compose.viewModel // Import for viewModel delegate

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ColorBloomTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Obtain the ViewModel instance
                    val gameViewModel: GameViewModel = viewModel()
                    // Display the GameScreen, passing grid state, score state, and tap handler
                    GameScreen(
                        gridState = gameViewModel.grid,
                        scoreState = gameViewModel.score,
                        comboMultiplierState = gameViewModel.comboMultiplier,
                        livesState = gameViewModel.lives,
                        isGameOverState = gameViewModel.isGameOver, // New argument
                        onSquareTap = { squareId -> gameViewModel.handleSquareTap(squareId) }
                    )
                }
            }
        }
    }
}

// Preview for the main app screen, now showing the BloomingGrid
// To make this preview work, you'd need a way to provide a GameViewModel instance
// or mock its state. For simplicity, we'll keep it focused on the actual app code.
// A common pattern is to have a top-level App Composable that handles ViewModel creation.

// @Preview(showBackground = true)
// @Composable
// fun DefaultPreview() { // Renamed from GreetingPreview
//     ColorBloomTheme {
//         // This preview would ideally show BloomingGrid with a mock ViewModel.
//         // For now, it won't render the grid correctly without a ViewModel.
//         // A simple placeholder or a more complex preview setup would be needed.
//         // Text("Preview: BloomingGrid would be here")
//
//         // Example of how you might try to make it work (might need adjustments)
//         // val previewVm: GameViewModel = viewModel() // This might not work in Preview context as expected
//         // BloomingGrid(gridState = previewVm.grid)
//     }
// }

// The Greeting and GreetingPreview functions can be removed or commented out.
/*
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ColorBloomTheme {
        Greeting("ColorBloom")
    }
}
*/
    }
}
