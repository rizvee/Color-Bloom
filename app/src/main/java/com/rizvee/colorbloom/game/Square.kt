package com.rizvee.colorbloom.game

import androidx.compose.ui.graphics.Color

enum class BloomState {
    IDLE,      // Not blooming, at initialColor
    BLOOMING,  // Transitioning from initialColor to targetColor
    PEAK,      // At targetColor (peak vibrancy)
    FADING     // Transitioning from targetColor back to initialColor
}

/**
 * Represents a single square on the game grid.
 *
 * @param id A unique identifier for the square.
 * @param initialColor The initial color of the square.
 * @param row The row index of the square in the grid.
 * @param col The column index of the square in the grid.
 * @param currentColor The current color of the square, which can change during animations.
 * @param currentBloomState The current stage of the blooming animation for this square.
 * @param targetColor The color the square will bloom into. Null if not set to bloom.
 */
data class Square(
    val id: Int,
    val row: Int,
    val col: Int,
    val initialColor: Color = Color.DarkGray,
    var currentColor: Color = initialColor,
    var currentBloomState: BloomState = BloomState.IDLE,
    var targetColor: Color? = null
) {
    // Future methods related to square state can use currentBloomState
    fun isTapEarly(): Boolean = currentBloomState == BloomState.BLOOMING
    fun isTapPerfect(): Boolean = currentBloomState == BloomState.PEAK
    fun isTapLate(): Boolean = currentBloomState == BloomState.FADING
}
