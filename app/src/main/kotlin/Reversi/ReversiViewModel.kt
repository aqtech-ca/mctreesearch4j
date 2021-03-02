package Reversi

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

class ReversiViewModel {
    val board: Array<Array<MutableState<ReversiSquare>>>
    private val state: ReversiState

    constructor(state: ReversiState) {
        this.state = state
        val squares = state.squares
        board = Array(squares.size) { Array(squares.size) { mutableStateOf(ReversiSquare.EMPTY) } }

        for (r in squares.indices) {
            for (c in squares[r].indices) {
                board[r][c].value = squares[r][c]
            }
        }
    }

    fun update() {
        val squares = state.squares
        for (r in squares.indices) {
            for (c in squares[r].indices) {
                if (squares[r][c] != board[r][c].value) {
                    board[r][c].value = squares[r][c]
                }
            }
        }
    }
}