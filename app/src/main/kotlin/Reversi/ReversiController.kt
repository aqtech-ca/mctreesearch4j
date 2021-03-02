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

        if (resolveFeasibleMoves(state, nextPlayer).isNotEmpty()) {
            state.currentPlayer = nextPlayer
        }
        else if (resolveFeasibleMoves(state).isEmpty()) {
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
    for (r in 0 until state.size) {
        for (c in 0 until state.size) {
            if (getAllFlips(state, Point(r, c), player).isNotEmpty()) {
                state.squares[r][c] = ReversiSquare.FEASIBLE
                moves.add(Point(r, c))
            }
            else if (state.squares[r][c] == ReversiSquare.FEASIBLE) {
                state.squares[r][c] = ReversiSquare.EMPTY
            }
        }
    }

    return moves
}

private fun getAllFlips(state: ReversiState, move: Point, player: ReversiSquare) : List<Point> {
    if (state.squares[move.x][move.y] != ReversiSquare.EMPTY &&
        state.squares[move.x][move.y] != ReversiSquare.FEASIBLE ) {
        return emptyList()
    }

    val flips = mutableListOf<Point>()

    // North
    flips.addAll(getFlips(state, move, player)  { p -> Point(p.x - 1, p.y) })
    // NorthEast
    flips.addAll(getFlips(state, move, player) { p -> Point(p.x - 1, p.y + 1) })
    // East
    flips.addAll(getFlips(state, move, player) { p -> Point(p.x, p.y + 1) })
    // SouthEast
    flips.addAll(getFlips(state, move, player) { p -> Point(p.x + 1, p.y + 1) })
    // South
    flips.addAll(getFlips(state, move, player) { p -> Point(p.x + 1, p.y) })
    // SouthWest
    flips.addAll(getFlips(state, move, player) { p -> Point(p.x + 1, p.y - 1) })
    // West
    flips.addAll(getFlips(state, move, player) { p -> Point(p.x, p.y - 1) })
    // NorthWest
    flips.addAll(getFlips(state, move, player) { p -> Point(p.x - 1, p.y - 1) })

    return flips
}

private fun getFlips(state: ReversiState, origin: Point, player: ReversiSquare, nextPoint: (p: Point) -> Point) : List<Point> {
    val flips = mutableListOf<Point>()
    var current = nextPoint(origin)

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
        if (square == ReversiSquare.FEASIBLE
            || square == ReversiSquare.EMPTY) {
            return emptyList()
        }

        // Flipping has completed
        if (square == player) {
            break
        }

        // Flip the current square
        flips.add(Point(current))

        current = nextPoint(current)
    }

    return flips
}
