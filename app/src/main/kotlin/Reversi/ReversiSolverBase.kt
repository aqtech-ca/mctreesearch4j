package Reversi

import StatefulSolver
import StatelessSolver
import java.awt.Point

open class ReversiSolverBase(initialState: ReversiState) : StatelessSolver<ReversiState, Point>(ReversiMDP(initialState), 2000, 1.4, 0.9, false) {
    fun getMove() : Point {
        constructTree(100)
        return extractOptimalAction()
    }
}