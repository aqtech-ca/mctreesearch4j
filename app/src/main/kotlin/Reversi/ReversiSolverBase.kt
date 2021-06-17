package Reversi

import ca.aqtech.mctreesearch4j.StatefulSolver
import ca.aqtech.mctreesearch4j.StatelessSolver
import java.awt.Point

open class ReversiSolverBase(protected val initialState: ReversiState) : StatefulSolver<ReversiState, Point>(ReversiMDP(initialState), 2000, 1.4, 0.9, false) {
    fun getMove() : Point {
        constructTree(5000)
        return extractOptimalAction()
    }
}