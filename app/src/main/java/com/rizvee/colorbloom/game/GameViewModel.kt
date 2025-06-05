package com.rizvee.colorbloom.game

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
// com.rizvee.colorbloom.game.Square should be automatically available due to same package

class GameViewModel : ViewModel() {

    private val _gridSize = 3 // For a 3x3 grid initially
    val gridSize: Int get() = _gridSize

    // Using MutableState to hold the grid, suitable for Compose observation
    private val _grid = mutableStateOf<List<List<Square>>>(emptyList())
    val grid: State<List<List<Square>>> = _grid

    init {
        initializeGrid()
    }

    private fun initializeGrid() {
        val newGrid = mutableListOf<List<Square>>()
        var idCounter = 0
        for (i in 0 until _gridSize) {
            val row = mutableListOf<Square>()
            for (j in 0 until _gridSize) {
                // Assuming Square is in the same package, direct instantiation is fine.
                row.add(Square(id = idCounter++, row = i, col = j, initialColor = Color.DarkGray))
            }
            newGrid.add(row)
        }
        _grid.value = newGrid
    }

    // Future methods for game logic will go here:
    // - Starting blooms
    // - Handling taps
    // - Updating scores
    // - Managing game progression
}
