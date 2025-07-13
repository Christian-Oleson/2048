package com.game2048

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
fun main() = application {
    val viewModel = remember { GameViewModel() }
    val gameState by viewModel.gameState.collectAsState()
    val scope = rememberCoroutineScope()
    
    Window(
        onCloseRequest = ::exitApplication,
        title = "2048",
        state = rememberWindowState(width = 500.dp, height = 600.dp),
        onKeyEvent = { keyEvent ->
            if (keyEvent.type == KeyEventType.KeyDown) {
                when (keyEvent.key) {
                    Key.DirectionUp -> {
                        viewModel.move(Direction.UP)
                        true
                    }
                    Key.DirectionDown -> {
                        viewModel.move(Direction.DOWN)
                        true
                    }
                    Key.DirectionLeft -> {
                        viewModel.move(Direction.LEFT)
                        true
                    }
                    Key.DirectionRight -> {
                        viewModel.move(Direction.RIGHT)
                        true
                    }
                    else -> false
                }
            } else {
                false
            }
        }
    ) {
        MaterialTheme {
            GameScreen(gameState, onResetGame = { viewModel.resetGame() })
        }
    }
}

@Composable
fun GameScreen(gameState: GameState, onResetGame: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Game title and score
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "2048",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF776E65)
            )
            
            Column(horizontalAlignment = Alignment.End) {
                Card(
                    modifier = Modifier.padding(4.dp),
                    backgroundColor = Color(0xFFBBADA0),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "SCORE",
                            fontSize = 12.sp,
                            color = Color(0xFFEEE4DA)
                        )
                        Text(
                            text = "${gameState.score}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
                
                Button(
                    onClick = onResetGame,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF8F7A66)),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text("New Game", color = Color.White)
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Game instructions
        Text(
            text = "Join the tiles, get to 2048!",
            fontSize = 18.sp,
            color = Color(0xFF776E65),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Text(
            text = "Use arrow keys to move the tiles.",
            fontSize = 14.sp,
            color = Color(0xFF776E65),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Game grid
        GameGrid(gameState.grid)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Game over or win message
        if (gameState.gameOver) {
            GameOverMessage(gameState.won, onResetGame)
        }
    }
}

@Composable
fun GameGrid(grid: Array<Array<Int>>) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .background(Color(0xFFBBADA0), RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            for (i in grid.indices) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    for (j in grid[i].indices) {
                        GameTile(
                            value = grid[i][j],
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .padding(4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GameTile(value: Int, modifier: Modifier = Modifier) {
    val backgroundColor = when (value) {
        0 -> Color(0xFFCDC1B4)
        2 -> Color(0xFFEEE4DA)
        4 -> Color(0xFFEDE0C8)
        8 -> Color(0xFFF2B179)
        16 -> Color(0xFFF59563)
        32 -> Color(0xFFF67C5F)
        64 -> Color(0xFFF65E3B)
        128 -> Color(0xFFEDCF72)
        256 -> Color(0xFFEDCC61)
        512 -> Color(0xFFEDC850)
        1024 -> Color(0xFFEDC53F)
        2048 -> Color(0xFFEDC22E)
        else -> Color(0xFF3C3A32) // For values > 2048
    }
    
    val textColor = if (value <= 4) Color(0xFF776E65) else Color.White
    
    Box(
        modifier = modifier
            .background(backgroundColor, RoundedCornerShape(4.dp)),
        contentAlignment = Alignment.Center
    ) {
        if (value != 0) {
            Text(
                text = "$value",
                fontSize = when (value) {
                    in 0..64 -> 24.sp
                    in 128..512 -> 20.sp
                    else -> 18.sp
                },
                fontWeight = FontWeight.Bold,
                color = textColor,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun GameOverMessage(won: Boolean, onResetGame: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0x80FFFFFF), RoundedCornerShape(8.dp))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = if (won) "You Won!" else "Game Over!",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = if (won) Color(0xFFEDC22E) else Color(0xFF776E65)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = onResetGame,
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF8F7A66))
            ) {
                Text("Try Again", color = Color.White)
            }
        }
    }
}