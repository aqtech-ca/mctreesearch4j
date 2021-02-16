package Reversi

import StatefulSolver
import java.awt.Point
import kotlin.random.Random

class ReversiSolver(private val initialState: ReversiState) {
    fun getMove(state: ReversiState) : Point {
        val game = ReversiMDP(state)
        val statefulSolver = StatefulSolver(
            game,
            Random.Default,
            2000,
            60,
            1.4,
            0.9,
            false
        )

        statefulSolver.buildTree()
        return statefulSolver.getNextOptimalAction()
    }
}