package mctreesearch4j

import kotlin.math.max

open class StatelessSolver<TState, TAction>(
    protected val mdp: MDP<TState, TAction>,
    protected val simulationDepthLimit: Int,
    explorationConstant: Double,
    protected val rewardDiscountFactor: Double,
    verbose: Boolean)
    : SolverBase<TAction, StatelessActionNode<TState, TAction>>(verbose, explorationConstant) {

    override var root = StatelessActionNode<TState, TAction>(null, null)

    init {
        simulateActions(root)
    }

    override fun selectNode(node: StatelessActionNode<TState, TAction>) : StatelessActionNode<TState, TAction> {
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

    override fun expandNode(node: StatelessActionNode<TState, TAction>): StatelessActionNode<TState, TAction> {
        // If the node is terminal, return it, except root node
        if (mdp.isTerminal(node.state)) {
            return node
        }

        // Expand an unexplored action
        val unexploredActions = node.validActions.minus(node.getChildren().map { c -> c.inducingAction })
        // Action cannot be null
        val actionTaken = unexploredActions.random() ?: throw Exception("No unexplored actions available")

        // Transition to new state for given action
        val newNode = StatelessActionNode(node, actionTaken)
        node.addChild(newNode)
        simulateActions(newNode)

        return newNode
    }

    override fun runSimulation(node: StatelessActionNode<TState, TAction>): Double {
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

    override fun updateNode(node: StatelessActionNode<TState, TAction>, reward: Double) {
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

    private fun simulateActions(node: StatelessActionNode<TState, TAction>) {
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