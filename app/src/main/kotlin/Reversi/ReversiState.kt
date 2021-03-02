package Reversi

class ReversiState : Cloneable {
    val squares: Array<Array<ReversiSquare>>
    val size: Int
    var currentPlayer: ReversiSquare

    constructor(size: Int) {
        squares = Array(size) {
            Array(size) {
                ReversiSquare.EMPTY
            }
        }

        squares[size/2 - 1][size/2 - 1] = ReversiSquare.LIGHT
        squares[size/2][size/2] = ReversiSquare.LIGHT
        squares[size/2 - 1][size/2] = ReversiSquare.DARK
        squares[size/2][size/2 - 1] = ReversiSquare.DARK

        squares[size/2][size/2 + 1] = ReversiSquare.FEASIBLE
        squares[size/2 + 1][size/2] = ReversiSquare.FEASIBLE
        squares[size/2 - 2][size/2 - 1] = ReversiSquare.FEASIBLE
        squares[size/2 - 1][size/2 - 2] = ReversiSquare.FEASIBLE

        this.size = size
        currentPlayer = ReversiSquare.DARK
    }

    constructor(otherState: ReversiState) {
        squares = Array(otherState.size) { emptyArray() }

        for (row in otherState.squares.indices) {
            squares[row] = otherState.squares[row].copyOf()
        }

        size = otherState.size
        currentPlayer = otherState.currentPlayer
    }

    public override fun clone(): ReversiState {
        return ReversiState(this)
    }
}