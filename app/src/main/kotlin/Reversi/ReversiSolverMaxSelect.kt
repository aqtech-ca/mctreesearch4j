package Reversi

import StateNode
import StatelessActionNode
import java.awt.Point

class ReversiSolverMaxSelect(initialState: ReversiState)
    : ReversiSolverBase(initialState) {

    override fun selectNode(node: StateNode<ReversiState, Point>): StateNode<ReversiState, Point> {
        // If the node is terminal, return it
        if (node.isTerminal) {
            return node
        }

        val exploredActions = node.exploredActions()

        assert(node.validActions.size >= exploredActions.size)

        // This state has not been fully explored
        if (node.validActions.size > exploredActions.size) {
            return node
        }

        return node.getChildren().map { c -> selectNode(c) }.maxByOrNull { n -> calculateUCT(n) } ?: node
    }
}