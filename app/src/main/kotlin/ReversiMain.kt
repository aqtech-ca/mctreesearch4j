package me.john_

import GridWorld.GridWorldGridSolve
import GridWorld.GridworldMDP
import GridWorld.GridworldReward
import GridWorld.GridworldState
import Reversi.*
import StatefulSolver
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
            if (i % 10 == 0) {
                println("${first.name} won by $dark to $light")
            }
            darkWins++
        } else if (dark < light) {
            if (i % 10 == 0) {
                println("${second.name} won by $light to $dark")
            }
            lightWins++
        } else {
            if (i % 10 == 0) {
                println("Tied $dark to $light")
            }
        }
    }

    println("${first.name} wins: $darkWins, ${second.name} wins: $lightWins")
}

fun main() {
//    ReversiGame().run()

    val elapsedMillis = measureTimeMillis {
//        var iterations = 100
//        var players = listOf(
//            ReversiPlayer({s -> ReversiSolverMinimax(s).getMove()}, "Minimax"),
//            ReversiPlayer({s -> ReversiSolverVanilla(s).getMove()}, "Base") )
//
//        simulate(players, iterations)
//        simulate(players.reversed(), iterations)

        val setRewards = listOf(
            GridworldReward(49, 87, -0.5),
            // GridworldReward(3, 3, 1.0),
            GridworldReward(39, 1, 1.0)
        )

//        var gridworld = GridworldMDP(
//            xSize = 10,
//            ySize = 10,
//            rewards = setRewards,
//            transitionProbability = 0.8,
//            startingLocation = GridworldState(2, 2, false)
//        )
//
//        var gwSolver = StatefulSolver(
//            gridworld,
//            99,
//            0.28,
//            0.95,
//            false
//        )
//        gwSolver.constructTree(999999)

        val gw = GridWorldGridSolve(
            50,
            50,
            setRewards,
            0.85
        )

        gw.getWorldSolve()
    }

    println("Simulation took $elapsedMillis ms")
}