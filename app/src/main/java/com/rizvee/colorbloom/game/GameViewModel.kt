package com.rizvee.colorbloom.game

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
package com.rizvee.colorbloom.game

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.coroutines.currentCoroutineContext
import kotlin.random.Random

class GameViewModel : ViewModel() {

    private val _gridSize = 3
    val gridSize: Int get() = _gridSize

    private val _grid = mutableStateOf<List<List<Square>>>(emptyList())
    val grid: State<List<List<Square>>> = _grid

    private val _score = mutableStateOf(0)
    val score: State<Int> = _score

    private val activeBloomJobs = mutableMapOf<Int, Job>()

    // Animation Durations
    companion object {
        private const val BLOOM_DURATION_MS = 600L // 0.6 seconds
        private const val PEAK_DURATION_MS = 200L  // 0.2 seconds (Perfect Bloom Window - Made narrower)
        private const val FADE_DURATION_MS = 500L  // 0.5 seconds
        private const val ANIMATION_STEP_MS = 16L // Aim for ~60 FPS updates
    }

    init {
        initializeGrid()
        triggerRandomBloom()
    }

    private fun initializeGrid() {
        val newGrid = MutableList(_gridSize) { r ->
            MutableList(_gridSize) { c ->
                Square(id = r * _gridSize + c, row = r, col = c, initialColor = Color.DarkGray)
            }
        }
        _grid.value = newGrid
    }

    private fun updateGridSnapshot() {
        _grid.value = _grid.value.map { row -> row.map { it.copy() } }
    }

    private fun findSquareById(id: Int): Square? {
        // Iterate through the grid to find the square by its ID
        // This is less efficient than direct row/col access but useful if only ID is known.
        for (row in _grid.value) {
            for (square in row) {
                if (square.id == id) {
                    return square
                }
            }
        }
        return null
    }

    // Public function to trigger a bloom on a random available square
    fun triggerRandomBloom() {
        val idleSquares = _grid.value.flatten().filter { it.currentBloomState == BloomState.IDLE && !activeBloomJobs.containsKey(it.id) }
        if (idleSquares.isEmpty()) {
            // println("No idle squares available to bloom.")
            return
        }

        val randomSquare = idleSquares.random()
        val bloomColors = listOf(Color.Red, Color.Green, Color.Blue, Color.Yellow, Color(0xFFFFA500) /*Orange*/, Color.Magenta, Color.Cyan)
        val randomColor = bloomColors.random()

        // Cancel any existing job for this square (shouldn't happen if filtering for IDLE and not in activeBloomJobs, but good for safety)
        activeBloomJobs.remove(randomSquare.id)?.cancel()

        val job = viewModelScope.launch {
            animateSquareBloom(randomSquare.id, randomColor)
        }
        activeBloomJobs[randomSquare.id] = job
        job.invokeOnCompletion {
            // Ensures removal from map regardless of completion reason (success, failure, cancellation)
            activeBloomJobs.remove(randomSquare.id)
            // If cancelled, animateSquareBloom should have reset it. If completed, it's also reset.
            // If it was cancelled by a tap, handleSquareTap would provide immediate visual feedback.
        }
    }

    private suspend fun animateSquareBloom(squareId: Int, targetBloomColor: Color) {
        val square = findSquareById(squareId) ?: return // Find square by ID now
        val initialColor = square.initialColor // Cache initial color

        // Enhanced reset square state function for cancellation, including flash
        val resetSquareWithFlash = suspend { // Make it a suspend lambda
            square.currentColor = Color.White // Flash white
            updateGridSnapshot()
            delay(100L) // Brief pause on white

            square.currentColor = initialColor // Then reset to actual initial color
            square.currentBloomState = BloomState.IDLE
            square.targetColor = null
            updateGridSnapshot()
        }

        if (!currentCoroutineContext().isActive) {
            resetSquareWithFlash() // Call suspend lambda
            return
        }

        square.targetColor = targetBloomColor

        // 1. Blooming Phase
        square.currentBloomState = BloomState.BLOOMING
        var elapsedTime = 0L
        while (elapsedTime < BLOOM_DURATION_MS) {
            if (!currentCoroutineContext().isActive) { resetSquareWithFlash(); return }
            val progress = elapsedTime.toFloat() / BLOOM_DURATION_MS
            square.currentColor = lerp(initialColor, targetBloomColor, progress)
            updateGridSnapshot()
            delay(ANIMATION_STEP_MS)
            elapsedTime += ANIMATION_STEP_MS
        }
        if (!currentCoroutineContext().isActive) { resetSquareWithFlash(); return }
        square.currentColor = targetBloomColor
        updateGridSnapshot()

        // 2. Peak Phase
        square.currentBloomState = BloomState.PEAK
        updateGridSnapshot()
        delay(PEAK_DURATION_MS) // Check for cancellation after delay
        if (!currentCoroutineContext().isActive) { resetSquareWithFlash(); return }


        // 3. Fading Phase
        square.currentBloomState = BloomState.FADING
        elapsedTime = 0L
        while (elapsedTime < FADE_DURATION_MS) {
            if (!currentCoroutineContext().isActive) { resetSquareWithFlash(); return }
            val progress = elapsedTime.toFloat() / FADE_DURATION_MS
            square.currentColor = lerp(targetBloomColor, initialColor, progress)
            updateGridSnapshot()
            delay(ANIMATION_STEP_MS)
            elapsedTime += ANIMATION_STEP_MS
        }
        if (!currentCoroutineContext().isActive) { resetSquareWithFlash(); return }
        square.currentColor = initialColor
        updateGridSnapshot()

        // 4. Reset to Idle (successful completion)
        square.currentBloomState = BloomState.IDLE
        square.targetColor = null
        updateGridSnapshot()
    }

    fun handleSquareTap(squareId: Int) {
        val square = findSquareById(squareId) ?: return

        val pointsEarned: Int
        val tapLogMessage: String

        when (square.currentBloomState) {
            BloomState.PEAK -> {
                pointsEarned = 100
                tapLogMessage = "Tap: Perfect on square $squareId. Score: +$pointsEarned"
            }
            BloomState.BLOOMING -> {
                pointsEarned = 50
                tapLogMessage = "Tap: Early on square $squareId. Score: +$pointsEarned"
            }
            BloomState.FADING -> {
                pointsEarned = 50
                tapLogMessage = "Tap: Late on square $squareId. Score: +$pointsEarned"
            }
            BloomState.IDLE -> {
                pointsEarned = 0
                tapLogMessage = "Tap: Miss on idle square $squareId. Score: +$pointsEarned"
            }
            // No else needed if all enum cases are covered, or handle unexpected states if necessary
        }
        println(tapLogMessage)
        _score.value += pointsEarned

        // If the square was actively blooming/peaking/fading
        if (square.currentBloomState != BloomState.IDLE) {
            activeBloomJobs.remove(squareId)?.cancel() // Cancel its animation job

            // Immediately reset its state for visual feedback
            square.currentBloomState = BloomState.IDLE
            square.currentColor = square.initialColor
            square.targetColor = null
            updateGridSnapshot()

            triggerRandomBloom() // Spawn a new bloom to replace the tapped one
        }
        // If an IDLE square was tapped (a pure miss), we do nothing further here.
        // No job to cancel, state is already IDLE, and we don't trigger a new bloom.
    }
}
