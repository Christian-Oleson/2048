package com.game2048

import kotlin.random.Random

/**
 * Main game class that handles the 2048 game logic.
 * This class is responsible for managing the game state, including the grid,
 * score, and game status (ongoing, won, lost).
 */
class Game(private val size: Int = 4) {
    // 2D array representing the game grid
    private var grid: Array<Array<Int>> = Array(size) { Array(size) { 0 } }
    
    // Current score
    private var _score: Int = 0
    val score: Int get() = _score
    
    // Game state
    private var _gameOver: Boolean = false
    val gameOver: Boolean get() = _gameOver
    
    private var _won: Boolean = false
    val won: Boolean get() = _won
    
    // Initialize the game with two random tiles
    init {
        addRandomTile()
        addRandomTile()
    }
    
    /**
     * Returns a copy of the current grid.
     */
    fun getGrid(): Array<Array<Int>> {
        return Array(size) { i -> Array(size) { j -> grid[i][j] } }
    }
    
    /**
     * Adds a random tile (2 or 4) to an empty cell in the grid.
     * Returns true if a tile was added, false if there are no empty cells.
     */
    private fun addRandomTile(): Boolean {
        val emptyCells = mutableListOf<Pair<Int, Int>>()
        
        // Find all empty cells
        for (i in 0 until size) {
            for (j in 0 until size) {
                if (grid[i][j] == 0) {
                    emptyCells.add(Pair(i, j))
                }
            }
        }
        
        // If there are no empty cells, return false
        if (emptyCells.isEmpty()) {
            return false
        }
        
        // Choose a random empty cell
        val (row, col) = emptyCells[Random.nextInt(emptyCells.size)]
        
        // Add a 2 (90% chance) or 4 (10% chance)
        grid[row][col] = if (Random.nextDouble() < 0.9) 2 else 4
        
        return true
    }
    
    /**
     * Moves tiles in the specified direction and merges them if possible.
     * Returns true if any tiles moved or merged, false otherwise.
     */
    fun move(direction: Direction): Boolean {
        // Save the current grid state to check if anything changed
        val previousGrid = getGrid()
        
        // Apply the move based on the direction
        val moved = when (direction) {
            Direction.UP -> moveUp()
            Direction.DOWN -> moveDown()
            Direction.LEFT -> moveLeft()
            Direction.RIGHT -> moveRight()
        }
        
        // If tiles moved, add a new random tile
        if (moved) {
            addRandomTile()
            
            // Check if the player has won (has a 2048 tile)
            if (!_won && hasWon()) {
                _won = true
            }
            
            // Check if the game is over (no more moves possible)
            if (!canMove()) {
                _gameOver = true
            }
        }
        
        return moved
    }
    
    /**
     * Checks if the player has won by having a 2048 tile.
     */
    private fun hasWon(): Boolean {
        for (i in 0 until size) {
            for (j in 0 until size) {
                if (grid[i][j] >= 2048) {
                    return true
                }
            }
        }
        return false
    }
    
    /**
     * Checks if any moves are possible.
     */
    private fun canMove(): Boolean {
        // If there are any empty cells, moves are possible
        for (i in 0 until size) {
            for (j in 0 until size) {
                if (grid[i][j] == 0) {
                    return true
                }
            }
        }
        
        // Check if any adjacent tiles can be merged
        for (i in 0 until size) {
            for (j in 0 until size - 1) {
                if (grid[i][j] == grid[i][j + 1]) {
                    return true
                }
            }
        }
        
        for (j in 0 until size) {
            for (i in 0 until size - 1) {
                if (grid[i][j] == grid[i + 1][j]) {
                    return true
                }
            }
        }
        
        return false
    }
    
    /**
     * Moves tiles up and merges them if possible.
     */
    private fun moveUp(): Boolean {
        var moved = false
        
        for (j in 0 until size) {
            // Compact non-zero tiles towards the top
            var writeIndex = 0
            for (i in 0 until size) {
                if (grid[i][j] != 0) {
                    if (i != writeIndex) {
                        grid[writeIndex][j] = grid[i][j]
                        grid[i][j] = 0
                        moved = true
                    }
                    writeIndex++
                }
            }
            
            // Merge tiles with the same value
            for (i in 0 until size - 1) {
                if (grid[i][j] != 0 && grid[i][j] == grid[i + 1][j]) {
                    grid[i][j] *= 2
                    _score += grid[i][j]
                    grid[i + 1][j] = 0
                    moved = true
                }
            }
            
            // Compact again after merging
            writeIndex = 0
            for (i in 0 until size) {
                if (grid[i][j] != 0) {
                    if (i != writeIndex) {
                        grid[writeIndex][j] = grid[i][j]
                        grid[i][j] = 0
                        moved = true
                    }
                    writeIndex++
                }
            }
        }
        
        return moved
    }
    
    /**
     * Moves tiles down and merges them if possible.
     */
    private fun moveDown(): Boolean {
        var moved = false
        
        for (j in 0 until size) {
            // Compact non-zero tiles towards the bottom
            var writeIndex = size - 1
            for (i in size - 1 downTo 0) {
                if (grid[i][j] != 0) {
                    if (i != writeIndex) {
                        grid[writeIndex][j] = grid[i][j]
                        grid[i][j] = 0
                        moved = true
                    }
                    writeIndex--
                }
            }
            
            // Merge tiles with the same value
            for (i in size - 1 downTo 1) {
                if (grid[i][j] != 0 && grid[i][j] == grid[i - 1][j]) {
                    grid[i][j] *= 2
                    _score += grid[i][j]
                    grid[i - 1][j] = 0
                    moved = true
                }
            }
            
            // Compact again after merging
            writeIndex = size - 1
            for (i in size - 1 downTo 0) {
                if (grid[i][j] != 0) {
                    if (i != writeIndex) {
                        grid[writeIndex][j] = grid[i][j]
                        grid[i][j] = 0
                        moved = true
                    }
                    writeIndex--
                }
            }
        }
        
        return moved
    }
    
    /**
     * Moves tiles left and merges them if possible.
     */
    private fun moveLeft(): Boolean {
        var moved = false
        
        for (i in 0 until size) {
            // Compact non-zero tiles towards the left
            var writeIndex = 0
            for (j in 0 until size) {
                if (grid[i][j] != 0) {
                    if (j != writeIndex) {
                        grid[i][writeIndex] = grid[i][j]
                        grid[i][j] = 0
                        moved = true
                    }
                    writeIndex++
                }
            }
            
            // Merge tiles with the same value
            for (j in 0 until size - 1) {
                if (grid[i][j] != 0 && grid[i][j] == grid[i][j + 1]) {
                    grid[i][j] *= 2
                    _score += grid[i][j]
                    grid[i][j + 1] = 0
                    moved = true
                }
            }
            
            // Compact again after merging
            writeIndex = 0
            for (j in 0 until size) {
                if (grid[i][j] != 0) {
                    if (j != writeIndex) {
                        grid[i][writeIndex] = grid[i][j]
                        grid[i][j] = 0
                        moved = true
                    }
                    writeIndex++
                }
            }
        }
        
        return moved
    }
    
    /**
     * Moves tiles right and merges them if possible.
     */
    private fun moveRight(): Boolean {
        var moved = false
        
        for (i in 0 until size) {
            // Compact non-zero tiles towards the right
            var writeIndex = size - 1
            for (j in size - 1 downTo 0) {
                if (grid[i][j] != 0) {
                    if (j != writeIndex) {
                        grid[i][writeIndex] = grid[i][j]
                        grid[i][j] = 0
                        moved = true
                    }
                    writeIndex--
                }
            }
            
            // Merge tiles with the same value
            for (j in size - 1 downTo 1) {
                if (grid[i][j] != 0 && grid[i][j] == grid[i][j - 1]) {
                    grid[i][j] *= 2
                    _score += grid[i][j]
                    grid[i][j - 1] = 0
                    moved = true
                }
            }
            
            // Compact again after merging
            writeIndex = size - 1
            for (j in size - 1 downTo 0) {
                if (grid[i][j] != 0) {
                    if (j != writeIndex) {
                        grid[i][writeIndex] = grid[i][j]
                        grid[i][j] = 0
                        moved = true
                    }
                    writeIndex--
                }
            }
        }
        
        return moved
    }
    
    /**
     * Resets the game to its initial state.
     */
    fun reset() {
        grid = Array(size) { Array(size) { 0 } }
        _score = 0
        _gameOver = false
        _won = false
        
        addRandomTile()
        addRandomTile()
    }
}

/**
 * Enum representing the four possible move directions.
 */
enum class Direction {
    UP, DOWN, LEFT, RIGHT
}