package Reversi

import StatefulSolver
import java.awt.Point
import kotlin.random.Random

class ReversiSolver(private val initialState: ReversiState) {
    fun getMove(state: ReversiState) : Point {
        val game = ReversiMDP(state)
        val statefulSolver = StatefulSolver(
            game,
            2000,
            1.4,
            0.9,
            false
        )

        statefulSolver.constructTree(60)
        return statefulSolver.extractOptimalAction()
    }
}