package ReversiMain

import Reversi.*
import ca.aqtech.mctreesearch4j.StatefulSolver
import java.awt.Point
import kotlin.system.measureTimeMillis

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
            if (i % 1 == 0) {
                println("${first.name} won by $dark to $light")
            }
            darkWins++
        } else if (dark < light) {
            if (i % 1 == 0) {
                println("${second.name} won by $light to $dark")
            }
            lightWins++
        } else {
            if (i % 1 == 0) {
                println("Tied $dark to $light")
            }
        }
    }

    println("${first.name} wins: $darkWins, ${second.name} wins: $lightWins")
}

fun adversarialSim() {
    val elapsedMillis = measureTimeMillis {
        var iterations = 20
        var players = listOf(
            ReversiPlayer({s -> ReversiSolverHeuristicSim(s).getMove()}, "Heuristic"),
            ReversiPlayer({s -> ReversiSolver(s).getMove()}, "Base") )

        simulate(players, iterations)
        simulate(players.reversed(), iterations)
    }

    println("Simulation took $elapsedMillis ms")
}

fun main() {
    ReversiGame().run()
}