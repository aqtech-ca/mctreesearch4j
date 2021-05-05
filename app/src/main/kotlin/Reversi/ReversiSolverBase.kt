package Reversi

import StatefulSolver
import StatelessSolver
import java.awt.Point

open class ReversiSolverBase(initialState: ReversiState, simulationDepth: Int = 2000) : StatefulSolver<ReversiState, Point>(ReversiMDP(initialState), simulationDepth, 1.4, 0.9, false) {
    fun getMove() : Point {
        constructTree(100)
        return extractOptimalAction()
    }
}