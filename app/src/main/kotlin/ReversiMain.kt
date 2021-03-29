package me.john_

import Reversi.*
import java.awt.Point

private class ReversiPlayer(val getMove: (ReversiState) -> Point, val name: String)

private fun simulate(players: List<ReversiPlayer>, iterations: Int)
{
    val first = players[0]
    val second = players[1]

    println("${first.name} playing Dark and goes first, ${second.name} playing Light")
    var lightWins = 0
    var darkWins = 0

    for (i in 1..iterations) {
        var state = ReversiState(8)

        while (state.currentPlayer != ReversiSquare.EMPTY) {
            if (state.currentPlayer == ReversiSquare.DARK) {
                ReversiController.executeMove(state, first.getMove(state.clone()))
            }
            if (state.currentPlayer == ReversiSquare.LIGHT) {
                ReversiController.executeMove(state, second.getMove(state.clone()))
            }
        }

        var dark = 0
        var light = 0
        for (square in state.squares.flatten()) {
            if (square == ReversiSquare.DARK) {
                dark++
            }
            else if (square == ReversiSquare.LIGHT) {
                light++
            }
        }

        if (dark > light) {
            println("${first.name} won by $dark to $light")
            darkWins++
        } else if (dark < light) {
            println("${second.name} won by $light to $dark")
            lightWins++
        } else {
            println("Tied $dark to $light")
        }
    }

    println("${first.name} wins: $darkWins, ${second.name} wins: $lightWins")
}

fun main() {
//    ReversiGame().run()

    var iterations = 2
    var players = listOf(
            ReversiPlayer({s -> ReversiSolverMinimax(s).getMove()}, "Minimax"),
            ReversiPlayer({s -> ReversiSolverVanilla(s).getMove()}, "Vanilla") )

    simulate(players, iterations)
    simulate(players.reversed(), iterations)
}