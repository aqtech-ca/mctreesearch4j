package Reversi

import androidx.compose.desktop.Window
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.*
import java.awt.Point

class ReversiGame {
    fun run() {
        val aiPlayer = mutableStateOf(ReversiSquare.EMPTY)
        val state = ReversiState(8)
        ReversiController.resolveFeasibleMoves(state)
        val viewModel = ReversiViewModel(state)
        val lastAIMove = mutableStateOf(Point(-1, -1))

        Window(
            title = "Reversi",
            size = IntSize(400, 450),
            resizable = false) {
            MaterialTheme {
                Column(Modifier.fillMaxSize()) {
                    var ai by remember { aiPlayer }
                    var lastMove by remember { lastAIMove }

                    Row(Modifier.weight(8.0f)) {
                        for (c in 0..7) {
                            Column(Modifier.weight(1.0f)) {
                                for (r in 0..7) {
                                    val square by remember { viewModel.board[r][c] }

                                    IconButton(
                                        modifier = Modifier
                                            .weight(1.0f)
                                            .fillMaxSize()
                                            .border(BorderStroke(0.1f.dp, Color(0xFF6200EE)))
                                            .background(Color(if (r == lastMove.x && c == lastMove.y) 0xFF8cee00 else 0xFFefe6fd)),
                                        onClick = {
                                            println("row: $r col: $c")
                                            if (state.squares[r][c] == ReversiSquare.FEASIBLE) {
                                                if (ReversiController.executeMove(state, Point(r, c))) {
                                                    ReversiController.resolveFeasibleMoves(state)
                                                    if (state.currentPlayer != ReversiSquare.EMPTY) {
                                                        while (state.currentPlayer == ai) {
                                                            println("AI is thinking")
                                                            lastMove =
                                                                ReversiSolverVanilla(state.clone()).getMove()
                                                            ReversiController.executeMove(state, lastMove)
                                                            ReversiController.resolveFeasibleMoves(state)
                                                        }
                                                        println("Next player: ${state.currentPlayer}")
                                                    }

                                                    if (ai != ReversiSquare.EMPTY){
                                                        var aiCount = 0
                                                        var playerCount = 0
                                                        val playerSquare = ReversiController.getOpponent(ai)
                                                        for (squareState in state.squares.flatten()) {
                                                            if (squareState == ai) {
                                                                aiCount++
                                                            } else if (squareState == playerSquare) {
                                                                playerCount++
                                                            }
                                                        }
                                                        println("Player Score: " + playerCount)
                                                        println("AI Score: " + aiCount)
                                                        if (state.currentPlayer == ReversiSquare.EMPTY) {
                                                            println("Game ended")
                                                            when {
                                                                playerCount > aiCount -> println("Player won by $playerCount to $aiCount")
                                                                playerCount < aiCount -> println("AI won by $aiCount to $playerCount")
                                                                else -> println("Tied $playerCount to $aiCount")
                                                            }
                                                        }
                                                    }

                                                    viewModel.update()
                                                }
                                            }
                                        }
                                    ) {
                                        when (square) {
                                            ReversiSquare.DARK -> Icon(Icons.Rounded.Phone,null)
                                            ReversiSquare.LIGHT -> Icon(Icons.Rounded.Star,null)
                                            ReversiSquare.FEASIBLE -> Icon(Icons.Rounded.Info,null)
                                            else -> {}
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Button(
                        modifier = Modifier
                            .weight(1.0f)
                            .fillMaxWidth()
                            .padding(5.dp),
                        enabled = ai == ReversiSquare.EMPTY,
                        onClick = {
                            ai = state.currentPlayer
                            lastMove = ReversiSolverVanilla(state.clone()).getMove()
                            ReversiController.executeMove(state, lastMove)
                            ReversiController.resolveFeasibleMoves(state)

                            viewModel.update()
                        }) {
                        Text("Start AI")
                    }
                }
            }
        }
    }
}