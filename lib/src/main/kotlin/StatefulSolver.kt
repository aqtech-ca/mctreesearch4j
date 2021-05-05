import kotlin.math.max

open class StatefulSolver<TState, TAction>(
    protected val mdp: MDP<TState, TAction>,
    protected val simulationDepthLimit: Int,
    explorationConstant: Double,
    protected val rewardDiscountFactor: Double,
    verbose: Boolean
) : SolverBase<TAction, StateNode<TState, TAction>>(verbose, explorationConstant) {

    override var root = createNode(null, null, mdp.initialState())

    override fun selectNode(node: StateNode<TState, TAction>): StateNode<TState, TAction> {
        // If the node is terminal, return it
        if (mdp.isTerminal(node.state)) {
            return node
        }

        val exploredActions = node.exploredActions()

        assert(node.validActions.size >= exploredActions.size)

        // This state has not been fully explored
        if (node.validActions.size > exploredActions.size) {
            return node
        }

        // This state has been explored, select best action
        var bestAction = exploredActions.first()
        var bestActionScore : Double? = null

        for (action in exploredActions) {
            val childrenOfAction = node.getChildren(action)
            val actionN = childrenOfAction.sumOf { c -> c.n }
            val actionReward = childrenOfAction.sumOf { c -> c.reward }
            val actionScore = calculateUCT(node.n, actionN, actionReward, explorationConstant)

            if (bestActionScore == null || actionScore > bestActionScore) {
                bestAction = action
                bestActionScore = actionScore
            }
        }

        val newState = mdp.transition(node.state, bestAction)

        val actionState = node.getChildren(bestAction).firstOrNull { s -> s.state == newState }
        // New state reached by an explored action
                ?: return createNode(node, bestAction, newState)


        // Existing state reached by an explored action
        return selectNode(actionState)
    }

    override fun expandNode(node: StateNode<TState, TAction>): StateNode<TState, TAction> {
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

    override fun runSimulation(node: StateNode<TState, TAction>): Double {
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

    override fun updateNode(node: StateNode<TState, TAction>, reward: Double) {
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

    private fun createNode(parent: StateNode<TState, TAction>?, inducingAction: TAction?, state: TState) : StateNode<TState, TAction> {
        val validActions = mdp.actions(state).toList()
        val isTerminal = mdp.isTerminal(state)
        val stateNode = StateNode(parent, inducingAction, state, validActions, isTerminal)

        parent?.addChild(stateNode)

        return stateNode
    }
}