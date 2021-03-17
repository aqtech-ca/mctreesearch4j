package Reversi

import StatefulSolver
import StatelessSolver
import java.awt.Point
import kotlin.random.Random

class ReversiSolver(private val initialState: ReversiState) {
    fun getMove(state: ReversiState) : Point {
        val game = ReversiMDP(state)
        val solver = StatelessSolver(
            game,
            2000,
            1.4,
            0.9,
            true
        )

        solver.constructTree(60)
        return solver.extractOptimalAction()
    }
}