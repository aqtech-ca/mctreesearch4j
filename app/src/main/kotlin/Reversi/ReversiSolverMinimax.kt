package Reversi

import mcts.StateNode
import mcts.StatelessActionNode
import java.awt.Point

class ReversiSolverHeuristicSim(initialState: ReversiState)
    : ReversiSolverBase(initialState) {

    override fun iterateStep() {
//        displayTree(4)
        super.iterateStep()
    }

    private val heuristicWeight = arrayOf(
        arrayOf(100, -10, 11, 6, 6, 11, -10, 100),
        arrayOf(-10, -20, 1, 2, 2, 1, -20, -10),
        arrayOf(10, 1, 5, 4, 4, 5, 1, 10),
        arrayOf(6, 2, 4, 2, 2, 4, 2, 6),
        arrayOf(6, 2, 4, 2, 2, 4, 2, 6),
        arrayOf(10, 1, 5, 4, 4, 5, 1, 10),
        arrayOf(-10, -20, 1, 2, 2, 1, -20, -10),
        arrayOf(100, -10, 11, 6, 6, 11, -10, 100),
        )

    override fun runSimulation(node: StateNode<ReversiState, Point>): Double {
        traceln("Simulation:")

        // If state is terminal, the reward is defined by MDP
        if (node.isTerminal) {
            traceln("Terminal state reached")

            return mdp.reward(node.parent?.state, node.inducingAction, node.state)
        }

        var depth = 0
        var currentState = node.state
        var discount = rewardDiscountFactor

        while(true) {
            val validActions = mdp.actions(currentState)

            var bestActionScore = Int.MIN_VALUE
            var bestActions = mutableListOf<Point>()

            for (action in validActions) {
                val score = heuristicWeight[action.x][action.y]
                if (score > bestActionScore)
                {
                    bestActionScore = score
                    bestActions = mutableListOf(action)
                }
                if (score == bestActionScore)
                {
                    bestActions.add(action)
                }
            }

            val randomAction = bestActions.random()
            val newState = mdp.transition(currentState, randomAction)

            if (verbose) {
                trace("-> $randomAction ")
                trace("-> $newState ")
            }

            if (mdp.isTerminal(newState)) {
                val reward = mdp.reward(currentState, randomAction, newState) * discount
                if (verbose) {
                    traceln("-> Terminal state reached : $reward")
                }

                return reward
            }

            currentState = newState
            depth++
            discount *= rewardDiscountFactor

            if (depth > simulationDepthLimit) {
                val reward = mdp.reward(currentState, randomAction, newState) * discount
                if (verbose) {
                    traceln("-> Depth limit reached: $reward")
                }

                return reward
            }
        }
    }

    override fun formatNode(node: StateNode<ReversiState, Point>): String {
        return "Depth: ${node.depth} Action: ${node.inducingAction}, Max Reward: ${"%.5f".format(node.maxReward)}"
    }
}