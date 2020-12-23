import kotlin.math.ln
import kotlin.math.max
import kotlin.math.sqrt
import kotlin.random.Random
import kotlin.text.StringBuilder

class StatefulSolver<TState, TAction>(
        private val mdp: MDP<TState, TAction>,
        private val random: Random,
        private val iterations: Int,
        private val simulationDepthLimit: Int,
        private val explorationConstant: Double,
        private val rewardDiscountFactor: Double,
        private val verbose: Boolean) {

    private var root : StateNode<TAction, TState>? = null

    fun buildTree() {
        initialize()

        for (i in 0..iterations) {
            iterateStep()
        }
    }

    fun initialize() {
        val initialState = mdp.initialState()//.randomElement(random)
        root = createStateNode(null, initialState)
    }

    fun iterateStep() {
        traceln("")
        traceln("New iteration")
        traceln("=============")

        // Selection
        val bestState = selectNode(root!!)

        if (verbose) {
            traceln("Selected:")
            displayNode(bestState)
        }

        // Expansion
        val expandedState = expandNode(bestState)

        if (verbose) {
            traceln("Expanding:")
            displayNode(expandedState)
        }

        // Simulation
        val simulatedReward = simulateState(expandedState)

        traceln("Simulated Reward: $simulatedReward")

        // Update
        updateNode(expandedState, simulatedReward)
    }

    private fun updateNode(stateNode: StateNode<TAction, TState>, simulatedReward: Double) {
        var currentStateNode = stateNode
        var currentReward = simulatedReward

        while (true)
        {
            currentStateNode.maxReward = max(currentReward, currentStateNode.maxReward)
            currentStateNode.reward += currentReward
            currentStateNode.n++

            val parentActionNode = currentStateNode.parentAction() ?: break
            parentActionNode.reward += currentReward
            parentActionNode.n++

            currentStateNode = parentActionNode.parentState()
            currentReward *= rewardDiscountFactor
        }
    }

    private fun simulateState(stateNode: StateNode<TAction, TState>) : Double {
        traceln("Simulation:")

        // If state is terminal, the reward is defined by MDP
        if (mdp.isTerminal(stateNode.state)) {
            traceln("Terminal state reached")

            return mdp.reward(stateNode.parentAction()?.parentState()?.state, stateNode.parentAction()?.action, stateNode.state)
        }

        var simulationRewards = 0.0
        val simulationIterations = 1

        repeat (simulationIterations) {
            simulationRewards += simulateStateIteration(stateNode.state)
        }

        return simulationRewards/simulationIterations
    }

    private fun simulateStateIteration(state: TState) : Double {
        var depth = 0
        var currentState = state
        var discount = rewardDiscountFactor

        while(true) {
            val validActions = mdp.actions(currentState)
            val randomAction = validActions.toList().random()
            val newState = mdp.transition(currentState, randomAction)//.randomElement(random)

            trace("-> $randomAction ")
            trace("-> $newState ")

            if (mdp.isTerminal(newState)) {
                val reward = mdp.reward(currentState, randomAction, newState) * discount
                traceln("-> Terminal state reached : $reward")

                return reward
            }

            currentState = newState
            depth += 2
            discount *= rewardDiscountFactor

            if (depth > simulationDepthLimit) {
                val reward = mdp.reward(state, randomAction, newState) * discount
                traceln("-> Depth limit reached: $reward")

                return reward
            }
        }
    }

    private fun expandNode(stateNode: StateNode<TAction, TState>) : StateNode<TAction, TState> {
        // If the node is terminal, return it
        if (stateNode.isTerminal) {
            return stateNode
        }

        // Expand an unexplored action
        val unexploredActions = stateNode.validActions.minus(stateNode.children.map { c -> c.action })
        val actionTaken = unexploredActions.random()
        val actionNode = createActionNode(stateNode, actionTaken)

        // Transition to new state for given action
        val newState = mdp.transition(stateNode.state, actionTaken)//.randomElement(random)
        return createStateNode(actionNode, newState)
    }

    private fun selectNode(stateNode: StateNode<TAction, TState>) : StateNode<TAction, TState> {
        // If the node is terminal, return it
        if (mdp.isTerminal(stateNode.state)) {
            return stateNode
        }

        // This state has not been fully explored
        if (stateNode.validActions!!.size != stateNode.children.size) {
            return stateNode
        }

        // This state has been explored, select best action
        val actionNode = stateNode.children.maxByOrNull { a -> calculateUCT(a) }

        val newState = mdp.transition(stateNode.state, actionNode!!.action)//.randomElement(random)

        val actionState = actionNode.children.firstOrNull { s -> s.state == newState }
                // New state reached by an explored action
                ?: return createStateNode(actionNode, newState)


        // Existing state reached by an explored action
        return selectNode(actionState)
    }

    // Utilities

    private fun createActionNode(parent: StateNode<TAction, TState>, action: TAction) : ActionNode<TAction, TState> {
        val actionNode = ActionNode<TAction, TState>(parent, action)
        parent.children.add(actionNode)

        return actionNode
    }

    private fun createStateNode(parent: ActionNode<TAction, TState>?, state: TState) : StateNode<TAction, TState> {
        val validActions = mdp.actions(state).toList()
        val isTerminal = mdp.isTerminal(state)
        val stateNode = StateNode(parent, state, validActions, isTerminal)

        parent?.children?.add(stateNode)

        return stateNode
    }

    private fun calculateUCT(node: NodeBase) : Double {
        val parentN = node.parent?.n ?: node.n
        return node.reward/node.n + explorationConstant*sqrt(ln(parentN.toDouble())/node.n)
    }

    // Debug and Diagnostics

    fun traceln(string: String) {
        if (verbose) {
            println(string)
        }
    }

    fun trace(string: String) {
        if (verbose) {
            print(string)
        }
    }

    fun displayTree() {
        if (root == null) return
        displayTree(root!!, "")
    }

    fun getNextOptimalAction(): TAction {
        val optimalAction = root!!.children.maxByOrNull { c -> c.n }
        return optimalAction!!.action
    }

    private fun displayNode(node: NodeBase) {
        if (node.parent != null) {
            displayNode(node.parent)
        }

        if (node.depth > 0) {
            print(" ".repeat((node.depth - 1)*2) + " └")
        }

        println(node)
    }

    private fun displayTree(node: NodeBase, indent: String) {
        if (node.depth > 3) {
            return
        }

        val line = StringBuilder()
                .append(indent)
                .append(" $node")
                .append(" (n: ${node.n}, reward: ${"%.5f".format(node.reward)}, UCT: ${"%.5f".format(calculateUCT(node))})")

        println(line.toString())

        if (node is Node<*>) {
            if (!node.children.any())
                return

            for (i in 0 until node.children.size - 1) {
                displayTree(node.children[i], generateIndent(indent) + " ├")
            }
            displayTree(node.children[node.children.size - 1], generateIndent(indent) + " └")
        }
    }

    private fun generateIndent(indent: String) : String {
        return indent.replace('├', '│').replace('└', ' ')
    }
}