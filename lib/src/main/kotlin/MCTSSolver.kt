import kotlin.math.ln
import kotlin.math.max
import kotlin.math.sqrt
import kotlin.random.Random
import kotlin.text.StringBuilder

class MCTSSolver<TState, TAction>(
        private val mdp: MDP<TState, TAction>,
        private val random: Random,
        private val iterations: Int,
        private val simulationDepthLimit: Int,
        private val explorationConstant: Double,
        private val rewardDiscountFactor: Double) {

    private var root : StateNode<TAction, TState>? = null
    private var states = mutableListOf<StateNode<TAction, TState>>()

    fun solve() {
        initialize()

        for (i in 0..iterations) {
            iterateStep()
        }
    }

    fun initialize() {
        val initialState = mdp.initialState().randomElement(random)
        val rootNode = StateNode<TAction, TState>(initialState, null)
        root = rootNode
        states.add(rootNode)
    }

    fun iterateStep() {
        iterateStep(false)
    }

    fun iterateStep(verbose: Boolean) {
        if (verbose) {
            println()
            println("New iteration")
            println("=============")
        }

        // Selection
        val bestState = selectNode(root!!)

        // Expansion
        val expandedState = expandNode(bestState)

        if (verbose) {
            println("Expanding:")
            displayNode(expandedState)
        }

        // Simulation
        val simulatedReward = simulateState(expandedState, verbose)

        // Update
        updateNode(expandedState, simulatedReward)
    }

    private fun updateNode(stateNode: StateNode<TAction, TState>, simulatedReward: Double) {
        var currentStateNode = stateNode
        var currentReward = simulatedReward

        while (true)
        {
            currentStateNode.reward = max(currentReward, currentStateNode.reward)
//            currentStateNode.reward += currentReward
            currentStateNode.n++

            var parentActionNode = currentStateNode.parentAction() ?: break
            parentActionNode.reward = max(currentReward, parentActionNode.reward)
//            parentActionNode.reward += currentReward
            parentActionNode.n++

            currentStateNode = parentActionNode.parentState()
            currentReward *= rewardDiscountFactor
        }
    }

    private fun simulateState(stateNode: StateNode<TAction, TState>, verbose: Boolean) : Double {
        if (verbose) {
            println("Simulating:")
        }

        // If state is terminal, the reward is defined by MDP
        if (mdp.isTerminal(stateNode.state)) {
            val reward = mdp.reward(stateNode.parentAction()?.parentState()?.state, stateNode.parentAction()?.action, stateNode.state)

            if (verbose) {
                println("Terminal reward: $reward")
            }

            return reward
        }

        var depth = stateNode.depth
        var state = stateNode.state
        var discount = rewardDiscountFactor

        while(true) {
            val validActions = mdp.actions(state)
            val randomAction = validActions.toList().random()
            val newState = mdp.transition(state, randomAction).randomElement(random)

            if (verbose) {
                println("$randomAction")
                println("$newState")
            }

            if (mdp.isTerminal(newState)) {
                val reward = mdp.reward(state, randomAction, newState) * discount

                if (verbose) {
                    println("Terminal reward: $reward")
                }

                return reward
            }

            state = newState
            depth += 2
            discount *= rewardDiscountFactor

            if (depth > simulationDepthLimit) {
                val reward = mdp.reward(state, randomAction, newState) * discount

                if (verbose) {
                    println("Depth limit reward: $reward")
                }

                return reward
            }
        }
    }

    private fun expandNode(stateNode: StateNode<TAction, TState>) : StateNode<TAction, TState> {
        // If the node is terminal, don't expand it
        if (mdp.isTerminal(stateNode.state)) {
            return stateNode
        }

        // For debugging
        if (stateNode.validActions == null) throw IllegalStateException()
        assert(stateNode.validActions!!.size > stateNode.children.size)

        val unexploredActions = stateNode.validActions!!.minus(stateNode.children.map { c -> c.action })
        val actionTaken = unexploredActions.random()
        val actionNode = ActionNode<TAction, TState>(actionTaken, stateNode)
        stateNode.children.add(actionNode)

        val newState = mdp.transition(stateNode.state, actionTaken).randomElement(random)
        val newStateNode = StateNode<TAction, TState>(newState, actionNode)
        actionNode.children.add(newStateNode)
        states.add(newStateNode)

        return newStateNode
    }

    private fun selectNode(stateNode: StateNode<TAction, TState>) : StateNode<TAction, TState> {
        var bestNode = states.maxByOrNull { a -> calculateUCT(a) }!!
        if (bestNode.validActions == null) {
            bestNode.validActions = mdp.actions(stateNode.state).toList()
        }
        return bestNode
    }

//    private fun selectNode(stateNode: StateNode<TAction, TState>) : StateNode<TAction, TState> {
//        // If the node is terminal, return it
//        if (mdp.isTerminal(stateNode.state)) {
//            return stateNode
//        }
//
//        // This state has never been explored
//        if (stateNode.validActions == null) {
//            stateNode.validActions = mdp.actions(stateNode.state).toList()
//            return stateNode
//        }
//
//        // This state has not been fully explored
//        if (stateNode.validActions!!.size != stateNode.children.size) {
//            return stateNode
//        }
//
//        // This state has been explored, select best action
//        val actionNode = stateNode.children.maxByOrNull { a -> calculateUCT(a) }
//
//        val newState = mdp.transition(stateNode.state, actionNode!!.action).randomElement(random)
//        val actionState = actionNode.children.firstOrNull { s -> s.state == newState }
//
//        // New state reached by an explored action
//        if (actionState == null) {
//            val newStateNode = StateNode<TAction, TState>(newState, actionNode)
//            newStateNode.validActions = mdp.actions(newState).toList()
//            actionNode.children.add(newStateNode)
//            states.add(newStateNode)
//            return newStateNode
//        }
//
//        // Existing state reached by an explored action
//        return selectNode(actionState)
//    }

    private fun calculateUCT(node: NodeBase) : Double {
        return node.reward/node.n + explorationConstant*sqrt(ln(root!!.n.toDouble()) /node.n)
    }

    fun display() {
        if (root == null) return
        displayTree(root!!, "")
    }

    fun displayOptimalPath() {
        var bestNode = states.maxByOrNull { n -> n.reward }!!
        displayNode(bestNode)
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
        val line = StringBuilder()
                .append(indent)
                .append(" $node")
                .append(" (n: ${node.n}, reward: ${node.reward}, UCT: ${calculateUCT(node)})")

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