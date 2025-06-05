package com.rizvee.colorbloom.game

import androidx.compose.ui.graphics.Color

/**
 * Represents a single square on the game grid.
 *
 * @param id A unique identifier for the square.
 * @param initialColor The initial color of the square.
 * @param row The row index of the square in the grid.
 * @param col The column index of the square in the grid.
 * @param currentColor The current color of the square, which can change during animations.
 */
data class Square(
    val id: Int,
    val row: Int,
    val col: Int,
    val initialColor: Color = Color.DarkGray, // Default to DarkGray as per design doc
    var currentColor: Color = initialColor // Initially, current color is the initial color
) {
    // Future methods related to square state (e.g., blooming, fading) could go here.
    // For example:
    // fun isBlooming(): Boolean = ...
    // fun isAtPeakVibrancy(): Boolean = ...
}
