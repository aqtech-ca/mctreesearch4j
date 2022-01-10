package Reversi

import ca.aqtech.mctreesearch4j.StatefulSolver
import java.awt.Point

open class ReversiSolver(protected val initialState: ReversiState) : StatefulSolver<ReversiState, Point>(ReversiMDP(initialState), 999, 1.4, 0.9, false) {
    fun getMove() : Point {
        runTreeSearch(999)
        return extractOptimalAction()!!
    }
}
