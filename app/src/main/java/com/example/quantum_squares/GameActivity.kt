package com.example.quantum_squares

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GameActivity : AppCompatActivity() {

    private val gridSize = 5
    private val maxParticles = 4
    private var currentPlayer = 1
    private val grid = Array(gridSize) { Array(gridSize) { Square() } }
    private var player1Score = 0
    private var player2Score = 0

    private lateinit var currentPlayerTextView: TextView
    private lateinit var player1ScoreTextView: TextView
    private lateinit var player2ScoreTextView: TextView

    data class Square(
        var particleCount: Int = 0,
        var owner: Int? = null // Null means no owner
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        currentPlayerTextView = findViewById(R.id.currentPlayerTextView)
        player1ScoreTextView = findViewById(R.id.player1Score)
        player2ScoreTextView = findViewById(R.id.player2Score)

        updatePlayerTurn()
        updateScore()

        // Set up click listeners for each LinearLayout in the grid
        setupClickListeners()
    }

    // Method to set up click listeners for each cell
    private fun setupClickListeners() {
        for (row in 0 until gridSize) {
            for (col in 0 until gridSize) {
                val cellId = resources.getIdentifier("cell${row * gridSize + col + 1}", "id", packageName)
                val cellLayout = findViewById<LinearLayout>(cellId)
                cellLayout.setOnClickListener {
                    onCellClicked(row, col)
                }
            }
        }
    }

    // Method to handle cell clicks
    private fun onCellClicked(row: Int, col: Int) {
        if (makeMove(row, col)) {
            updateGridCellUI(row, col)
        }
    }

    // Method to make a move
    fun makeMove(row: Int, col: Int): Boolean {
        if (row !in 0 until gridSize || col !in 0 until gridSize) return false
        val square = grid[row][col]

        // Check if the current player can add a particle here
        if (square.owner == null || square.owner == currentPlayer) {
            // Add a particle and set the owner to the current player
            square.particleCount++
            square.owner = currentPlayer

            // Update UI
            updateGridCellUI(row, col)

            // Check for collapse
            if (square.particleCount >= maxParticles) {
                handleCollapse(row, col)
            }

            // Check if the game is over
            if (isGameOver()) {
                endGame()
            } else {
                // Switch player turn
                switchPlayer()
                updatePlayerTurn()
            }
            return true
        }
        return false
    }

    // Handle collapsing squares and chain reactions
    private fun handleCollapse(row: Int, col: Int) {
        val square = grid[row][col]
        square.particleCount = 0
        square.owner = null

        // Award a point for controlling the square
        if (currentPlayer == 1) {
            player1Score++
        } else {
            player2Score++
        }

        // Update score UI
        updateScore()

        // Distribute particles to adjacent squares (up, down, left, right)
        listOf(Pair(row - 1, col), Pair(row + 1, col), Pair(row, col - 1), Pair(row, col + 1)).forEach { (r, c) ->
            if (r in 0 until gridSize && c in 0 until gridSize) {
                grid[r][c].particleCount++
                updateGridCellUI(r, c)
                // Check for further collapses
                if (grid[r][c].particleCount >= maxParticles) {
                    handleCollapse(r, c)
                }
            }
        }
    }

    // Update the text for current player's turn
    private fun updatePlayerTurn() {
        currentPlayerTextView.text = "Turn: Player $currentPlayer"
    }

    // Update the scores for both players
    private fun updateScore() {
        player1ScoreTextView.text = "Player 1 Score: $player1Score"
        player2ScoreTextView.text = "Player 2 Score: $player2Score"
    }

    // End the game and show the winner
    private fun endGame() {
        val winner = if (player1Score >= 10) "Player 1" else "Player 2"
        currentPlayerTextView.text = "Game Over! $winner wins!"
        // Disable further moves (if needed, you can disable all click listeners)
    }

    // Update UI for a specific grid cell based on row and column
    private fun updateGridCellUI(row: Int, col: Int) {
        val cellTextViewId = resources.getIdentifier("textView${row * gridSize + col + 1}", "id", packageName)
        val cellImageViewId = resources.getIdentifier("imageView${row * gridSize + col + 1}", "id", packageName)

        val cellTextView = findViewById<TextView>(cellTextViewId)
        val cellImageView = findViewById<ImageView>(cellImageViewId)

        cellTextView.text = "${grid[row][col].particleCount}"
        cellImageView.setBackgroundResource(if (currentPlayer == 1) R.drawable.green_circle else R.drawable.red_circle_svgrepo_com)
    }

    // Switch the player turn
    private fun switchPlayer() {
        currentPlayer = if (currentPlayer == 1) 2 else 1
    }

    // Check if the game is over
    fun isGameOver(): Boolean {
        return player1Score >= 10 || player2Score >= 10 ||
                grid.all { row -> row.all { it.particleCount > 0 } }
    }

    // Get current player turn
    fun getCurrentPlayer(): Int {
        return currentPlayer
    }
}