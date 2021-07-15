package ca.aqtech.mctreesearch4j

import kotlin.math.max

open class GenericSolver<StateType, ActionType>(
    private val mdp: MDP<StateType, ActionType>,
    private val simulationDepthLimit: Int,
    explorationConstant: Double,
    private val rewardDiscountFactor: Double,
    verbose: Boolean
) : Solver<ActionType, ActionNode<StateType, ActionType>>(verbose, explorationConstant) {

    final override var root = ActionNode<StateType, ActionType>(null, null)

    init {
        simulateActions(root)
    }

    override fun select(node: ActionNode<StateType, ActionType>): ActionNode<StateType, ActionType> {
        // If this node is a leaf node, return it
        if (node.getChildren().isEmpty()) {
            return node
        }

        var currentNode = node
        simulateActions(node)

        // Run a simulation greedily
        while (true) {
            if (mdp.isTerminal(currentNode.state)) {
                return currentNode
            }

            val currentChildren = currentNode.getChildren()
            val exploredActions = currentChildren.map { c -> c.inducingAction }

            if (currentNode.validActions.minus(exploredActions).any()) {
                // There are unexplored actions
                return currentNode
            }

            // All actions have been explored, choose best one
            currentNode = currentChildren.maxByOrNull { a -> calculateUCT(a) } ?: throw Exception("There were no children for explored node")
            simulateActions(currentNode)
        }
    }

    override fun expand(node: ActionNode<StateType, ActionType>): ActionNode<StateType, ActionType> {
        // If the node is terminal, return it, except root node
        if (mdp.isTerminal(node.state)) {
            return node
        }

        // Expand an unexplored action
        val unexploredActions = node.validActions.minus(node.getChildren().map { c -> c.inducingAction })
        // Action cannot be null
        val actionTaken = unexploredActions.random() ?: throw Exception("No unexplored actions available")

        // Transition to new state for given action
        val newNode = ActionNode(node, actionTaken)
        node.addChild(newNode)
        simulateActions(newNode)

        return newNode
    }

    override fun simulate(node: ActionNode<StateType, ActionType>): Double {
        traceln("Simulation:")

        // If state is terminal, the reward is defined by MDP
        if (mdp.isTerminal(node.state)) {
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

    override fun backpropagate(node: ActionNode<StateType, ActionType>, reward: Double) {
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

    private fun simulateActions(node: ActionNode<StateType, ActionType>) {
        val parent = node.parent

        if (parent == null) {
            val initialState = mdp.initialState()
            node.state = initialState
            node.validActions = mdp.actions(initialState)
            return
        }

        // Parent simulation must be run before current simulation can proceed
        val parentState = parent.state
        // If the parent node is not null, a parent action must have been specified, otherwise it's an error
        val parentAction = node.inducingAction ?: throw Exception("Action was null for non-null parent")

        val state = mdp.transition(parentState, parentAction)
        node.state = state
        node.validActions = mdp.actions(state)
    }
}