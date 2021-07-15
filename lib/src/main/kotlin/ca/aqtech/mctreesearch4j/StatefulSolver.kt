package ca.aqtech.mctreesearch4j

import kotlin.math.max

open class StatefulSolver<StateType, ActionType> (
    protected val mdp: MDP<StateType, ActionType>,
    protected val simulationDepthLimit: Int,
    explorationConstant: Double,
    protected val rewardDiscountFactor: Double,
    verbose: Boolean
) : Solver<ActionType, StateNode<StateType, ActionType>>(verbose, explorationConstant) {

    override var root = createNode(null, null, mdp.initialState())

    override fun select(node: StateNode<StateType, ActionType>): StateNode<StateType, ActionType> {
        var currentNode = node
        while(true) {
            // If the node is terminal, return it
            if (mdp.isTerminal(currentNode.state)) {
                return currentNode
            }

            val exploredActions = currentNode.exploredActions()
            assert(currentNode.validActions.size >= exploredActions.size)

            // This state has not been fully explored
            if (currentNode.validActions.size > exploredActions.size) {
                return currentNode
            }

            // This state has been explored, select best action
            currentNode = currentNode.getChildren().maxByOrNull { a -> calculateUCT(a) } ?: throw Exception("There were no children for explored node")
        }
    }

    override fun expand(node: StateNode<StateType, ActionType>): StateNode<StateType, ActionType> {
        // If the node is terminal, return it
        if (node.isTerminal) {
            return node
        }

        // Expand an unexplored action
        val unexploredActions = node.validActions.minus(node.getChildren().map { c -> c.inducingAction }).distinct()
        val actionTaken = unexploredActions.random() ?: throw Exception("No unexplored actions available")

        // Transition to new state for given action
        val newState = mdp.transition(node.state, actionTaken)
        return createNode(node, actionTaken, newState)
    }

    override fun simulate(node: StateNode<StateType, ActionType>): Double {
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
            val randomAction = validActions.random()
            val newState = mdp.transition(currentState, randomAction)

            if (verbose)
            {
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

    override fun backpropagate(node: StateNode<StateType, ActionType>, reward: Double) {
        var currentStateNode = node
        var currentReward = reward

        while (true)
        {
            currentStateNode.maxReward = max(currentReward, currentStateNode.maxReward)
            currentStateNode.reward += currentReward
            currentStateNode.n++

            currentStateNode = currentStateNode.parent ?: break
            currentReward *= rewardDiscountFactor
        }
    }

    // Utilities

    private fun createNode(parent: StateNode<StateType, ActionType>?, inducingAction: ActionType?, state: StateType): StateNode<StateType, ActionType> {
        val validActions = mdp.actions(state).toList()
        val isTerminal = mdp.isTerminal(state)
        val stateNode = StateNode(parent, inducingAction, state, validActions, isTerminal)

        parent?.addChild(stateNode)

        return stateNode
    }
}