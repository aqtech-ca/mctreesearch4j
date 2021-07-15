package Reversi
import ca.aqtech.mctreesearch4j.StatefulSolver
import ca.aqtech.mctreesearch4j.SimpleSolver

import java.awt.Point

open class ReversiStatelessSolver(protected val initialState: ReversiState) : SimpleSolver<ReversiState, Point>(ReversiMDP(initialState), 2000, 1.4, 0.9, false) {
    fun getMove() : Point {
        runTreeSearch(500)
        return extractOptimalAction()!!
    }
}
