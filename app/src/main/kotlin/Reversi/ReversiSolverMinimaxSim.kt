package Reversi

import StateNode
import StatelessActionNode
import java.awt.Point

class ReversiSolverMinimaxSim(initialState: ReversiState)
    : ReversiSolverBase(initialState, 3) {

    override fun runSimulation(node: StateNode<ReversiState, Point>): Double {
        // If state is terminal, the reward is defined by MDP
        if (mdp.isTerminal(node.state)) {
            traceln("Terminal state reached")

            return mdp.reward(node.parent?.state, node.inducingAction, node.state)
        }

        val simulationRoot = StateNode(null, null, node.state, mdp.actions(node.state), mdp.isTerminal(node.state))
        populateMoves(simulationRoot)

//        displayTree(simulationRoot, simulationDepthLimit)

        val best = minimaxSelect(simulationRoot)

        return mdp.reward(best.parent?.state, best.inducingAction, best.state)
    }

    private fun populateMoves(node: StateNode<ReversiState, Point>) {
        // If the node is terminal or limit reached
        if (node.isTerminal || node.depth > simulationDepthLimit) {
            return
        }

        for (action in node.validActions) {
            appendNode(node, action)
        }

        for (child in node.getChildren()) {
            populateMoves(child)
        }
    }

    private fun appendNode(parent: StateNode<ReversiState, Point>, inducingAction: Point){
        val state = mdp.transition(parent.state, inducingAction)
        val validActions = mdp.actions(state).toList()
        val isTerminal = mdp.isTerminal(state)
        val stateNode = StateNode(parent, inducingAction, state, validActions, isTerminal)

        parent?.addChild(stateNode)
    }

    private fun minimaxSelect(node: StateNode<ReversiState, Point>): StateNode<ReversiState, Point> {
        // If the node is terminal or is a leaf node
        if (node.isTerminal || node.getChildren().isEmpty()) {
            return node
        }

        return node.getChildren().map { c -> minimaxSelect(c) }.maxByOrNull { n -> minimaxFactor(n)*mdp.reward(n.parent?.state, n.inducingAction, n.state) } ?: node
    }

    private fun minimaxFactor(node: StateNode<ReversiState, Point>): Int {
        return if (node.depth % 2 == 0) 1 else -1
    }

    override fun formatNode(node: StateNode<ReversiState, Point>): String {
        return "Depth: ${node.depth} Action: ${node.inducingAction}, Max Reward: ${"%.5f".format(node.maxReward)}"
    }
}