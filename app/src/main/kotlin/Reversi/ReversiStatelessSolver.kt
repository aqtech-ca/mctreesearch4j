package Reversi

import ca.aqtech.mctreesearch4j.GenericSolver
import java.awt.Point

open class ReversiStatelessSolver(protected val initialState: ReversiState) : GenericSolver<ReversiState, Point>(ReversiMDP(initialState), 1000, 1.4, 0.9, false) {
    fun getMove() : Point {
        runTreeSearch(500)
        return extractOptimalAction()!!
    }
}
