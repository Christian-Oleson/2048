package com.game2048

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel for the 2048 game.
 * This class provides a reactive interface to the game logic for the UI.
 */
class GameViewModel {
    private val game = Game()
    
    // Game state as a StateFlow that UI can observe
    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()
    
    init {
        updateGameState()
    }
    
    /**
     * Moves tiles in the specified direction.
     */
    fun move(direction: Direction) {
        val moved = game.move(direction)
        if (moved) {
            updateGameState()
        }
    }
    
    /**
     * Resets the game to its initial state.
     */
    fun resetGame() {
        game.reset()
        updateGameState()
    }
    
    /**
     * Updates the game state based on the current game.
     */
    private fun updateGameState() {
        _gameState.update {
            GameState(
                grid = game.getGrid(),
                score = game.score,
                gameOver = game.gameOver,
                won = game.won
            )
        }
    }
}

/**
 * Data class representing the state of the game.
 */
data class GameState(
    val grid: Array<Array<Int>> = Array(4) { Array(4) { 0 } },
    val score: Int = 0,
    val gameOver: Boolean = false,
    val won: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as GameState

        if (!grid.contentDeepEquals(other.grid)) return false
        if (score != other.score) return false
        if (gameOver != other.gameOver) return false
        if (won != other.won) return false

        return true
    }

    override fun hashCode(): Int {
        var result = grid.contentDeepHashCode()
        result = 31 * result + score
        result = 31 * result + gameOver.hashCode()
        result = 31 * result + won.hashCode()
        return result
    }
}