package ReversiController

import Reversi.*
import java.awt.Point
import java.util.*

private val PLAYERS = EnumSet.of(ReversiSquare.LIGHT, ReversiSquare.DARK)

fun getOpponent(player: ReversiSquare) : ReversiSquare {
    return PLAYERS.minus(player).single()
}

fun executeMove(state: ReversiState, move: Point) : Boolean {
    val flips = getAllFlips(state, move, state.currentPlayer)

    if (flips.isNotEmpty()) {
        // Flip all
        for (flip in flips) {
            state.squares[flip.x][flip.y] = state.currentPlayer
        }
        state.squares[move.x][move.y] = state.currentPlayer

        val nextPlayer = getOpponent(state.currentPlayer)

        if (anyFeasibleMoves(state, nextPlayer)) {
            state.currentPlayer = nextPlayer
        }
        else if (!anyFeasibleMoves(state, state.currentPlayer)) {
            state.currentPlayer = ReversiSquare.EMPTY
        }
    }

    return flips.isNotEmpty()
}

fun resolveFeasibleMoves(state: ReversiState) : List<Point> {
    return resolveFeasibleMoves(state, state.currentPlayer)
}

fun resolveFeasibleMoves(state: ReversiState, player: ReversiSquare) : List<Point> {
    val moves = mutableListOf<Point>()
    for (r in state.squares.indices) {
        for (c in state.squares.indices) {
            if (anyFlips(state, Point(r, c), player)) {
                moves.add(Point(r, c))
            }
        }
    }

    return moves
}

fun anyFeasibleMoves(state: ReversiState, player: ReversiSquare) : Boolean {
    for (r in 0 until state.size) {
        for (c in 0 until state.size) {
            if (anyFlips(state, Point(r, c), player)) {
                return true
            }
        }
    }

    return false
}

private fun anyFlips(state: ReversiState, move: Point, player: ReversiSquare) : Boolean {
    if (state.squares[move.x][move.y] != ReversiSquare.EMPTY) {
        return false
    }

    if (anyFlips(state, move, player) { p -> north(p) }) return true
    if (anyFlips(state, move, player) { p -> north(p) ; east(p) }) return true
    if (anyFlips(state, move, player) { p -> east(p) }) return true
    if (anyFlips(state, move, player) { p -> south(p) ; east(p) }) return true
    if (anyFlips(state, move, player) { p -> south(p) }) return true
    if (anyFlips(state, move, player) { p -> south(p) ; west(p) }) return true
    if (anyFlips(state, move, player) { p -> west(p) }) return true
    if (anyFlips(state, move, player) { p -> north(p) ; west(p) }) return true

    return false
}

private fun anyFlips(state: ReversiState, origin: Point, player: ReversiSquare, nextPoint: (p: Point) -> Unit) : Boolean {
    var flipped = false
    val current = Point(origin)
    nextPoint(current)

    while (true) {
        // Out of bounds
        if (current.x < 0
            || current.y < 0
            || current.x >= state.size
            || current.y >= state.size) {

            return false
        }

        val square = state.squares[current.x][current.y]

        // Unflippable
        if (square == ReversiSquare.EMPTY) {
            return false
        }

        // Flipping has completed
        if (square == player) {
            break
        }

        // Flipped the current square
        flipped = true

        nextPoint(current)
    }

    return flipped
}

private fun getAllFlips(state: ReversiState, move: Point, player: ReversiSquare) : List<Point> {
    if (state.squares[move.x][move.y] != ReversiSquare.EMPTY ) {
        return emptyList()
    }

    val flips = mutableListOf<Point>()

    flips.addAll(getFlips(state, move, player) { p -> north(p) })
    flips.addAll(getFlips(state, move, player) { p -> north(p) ; east(p) })
    flips.addAll(getFlips(state, move, player) { p -> east(p) })
    flips.addAll(getFlips(state, move, player) { p -> south(p) ; east(p) })
    flips.addAll(getFlips(state, move, player) { p -> south(p) })
    flips.addAll(getFlips(state, move, player) { p -> south(p) ; west(p) })
    flips.addAll(getFlips(state, move, player) { p -> west(p) })
    flips.addAll(getFlips(state, move, player) { p -> north(p) ; west(p) })

    return flips
}

private fun getFlips(state: ReversiState, origin: Point, player: ReversiSquare, nextPoint: (p: Point) -> Unit) : List<Point> {
    var flips : MutableList<Point>? = null
    val current = Point(origin)
    nextPoint(current)

    while (true) {
        // Out of bounds
        if (current.x < 0
            || current.y < 0
            || current.x >= state.size
            || current.y >= state.size) {

            return emptyList()
        }

        val square = state.squares[current.x][current.y]

        // Unflippable
        if (square == ReversiSquare.EMPTY) {
            return emptyList()
        }

        // Flipping has completed
        if (square == player) {
            break
        }

        // Flip the current square
        if (flips == null)
        {
            flips = mutableListOf()
        }
        flips.add(Point(current))

        nextPoint(current)
    }

    return flips ?: emptyList()
}

private fun north(p: Point) {
    p.x--
}

private fun east(p: Point) {
    p.y++
}

private fun south(p: Point) {
    p.x++
}

private fun west(p: Point) {
    p.y--
}
