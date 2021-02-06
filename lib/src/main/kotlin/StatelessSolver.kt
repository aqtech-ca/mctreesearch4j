import kotlin.math.ln
import kotlin.math.max
import kotlin.math.sqrt
import kotlin.random.Random
import kotlin.text.StringBuilder

class StatelessSolver<TState, TAction>(
        private val mdp: MDP<TState, TAction>,
        private val random: Random,
        private val iterations: Int,
        private val simulationDepthLimit: Int,
        private val explorationConstant: Double,
        private val rewardDiscountFactor: Double,
        private val verbose: Boolean) {

    private var root : StateActionNode<TAction>? = null

    fun buildTree() {
        initialize()

        for (i in 0 until iterations) {
            iterateStep()
        }
    }

    fun initialize() {
        root = createStateActionNode(null, null)
    }

    fun iterateStep() {
        traceln("")
        traceln("New iteration")
        traceln("=============")

        // Selection
        val best = selectNode(root!!)

        if (verbose) {
            traceln("Selected:")
            displayNode(best.node)
        }

        // Expansion
        val expanded = expandNode(best)

        if (verbose) {
            traceln("Expanding:")
            displayNode(expanded.node)
        }

        // Simulation
        val simulatedReward = simulateState(expanded)

        traceln("Simulated Reward: $simulatedReward")

        // Update
        updateNode(expanded.node, simulatedReward)
    }

    private fun updateNode(stateNode: StateActionNode<TAction>, simulatedReward: Double) {
        var currentStateNode = stateNode as NodeBase
        var currentReward = simulatedReward

        while (true)
        {
            currentStateNode.maxReward = max(currentReward, currentStateNode.maxReward)
            currentStateNode.reward += currentReward
            currentStateNode.n++

            currentStateNode = currentStateNode.parent ?: break

            currentReward *= rewardDiscountFactor
        }
    }

    private fun simulateState(simulationState: SimulationState<TAction, TState>) : Double {
        traceln("Simulation:")

        // If state is terminal, the reward is defined by MDP
        if (mdp.isTerminal(simulationState.state)) {
            traceln("Terminal state reached")

            return mdp.reward(simulationState.previousState, simulationState.node.parentAction, simulationState.state)
        }

        var simulationRewards = 0.0
        val simulationIterations = 1

        repeat (simulationIterations) {
            simulationRewards += simulateStateIteration(simulationState.state)
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
            val newState = mdp.transition(currentState, randomAction)

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

    private fun expandNode(simulationState: SimulationState<TAction, TState>) : SimulationState<TAction, TState> {
        // If the node is terminal, return it
        if (mdp.isTerminal(simulationState.state)) {
            return simulationState
        }

        // Expand an unexplored action
        val unexploredActions = simulationState.validActions.minus(simulationState.node.children.map { c -> c.parentAction })
        // Action cannot be null
        val actionTaken = unexploredActions.random() ?: throw Exception("No unexplored actions available")

        val newNode = createStateActionNode(simulationState.node, actionTaken)

        // Transition to new state for given action
        val newState = mdp.transition(simulationState.state, actionTaken)

        return SimulationState(newNode, simulationState.state, newState, mdp.actions(newState))
    }

    private fun selectNode(node: StateActionNode<TAction>) : SimulationState<TAction, TState> {
        // If this node is a leaf node, return it
        if (node.children.isEmpty()) {
            return simulateActions(node)
        }

        var currentSimulation = simulateActions(node)

        // Run a simulation greedily
        while (true) {
            if (mdp.isTerminal(currentSimulation.state)) {
                return currentSimulation
            }

            val exploredActions = currentSimulation.node.children.map { c -> c.parentAction}

            if (currentSimulation.validActions.minus(exploredActions).any()) {
                // There are unexplored actions
                return currentSimulation
            }

            // All actions have been explored, choose best one
            val newNode = currentSimulation.node.children.maxByOrNull { a -> calculateUCT(a) } ?: throw Exception("There were no children for explored node")
            val newState = mdp.transition(currentSimulation.state, newNode.parentAction ?: throw Exception("Parent action expected"))

            currentSimulation = SimulationState(newNode, currentSimulation.state, newState, mdp.actions(newState))
        }
    }

    // Utilities

    private fun createStateActionNode(parent: StateActionNode<TAction>?, action: TAction?) : StateActionNode<TAction> {
        if (parent == null && action != null || parent != null && action == null) {
            // The action is null if and only if parent is null
            throw Exception("Parent and action must both be null or non-null")
        }

        val newNode = StateActionNode(parent, action)

        parent?.children?.add(newNode)

        return newNode
    }

    private fun simulateActions(node: StateActionNode<TAction>) : SimulationState<TAction, TState> {
        val parent = node.parentStateAction()

        if (parent == null) {
            val initialState = mdp.initialState()
            return SimulationState(node, null, initialState, mdp.actions(initialState))
        }
        // If the parent node is not null, a parent action must have been specified, otherwise it's an error
        val parentAction = node.parentAction ?: throw Exception("Action was null for non-null parent")

        val parentSimulation = simulateActions(parent)

        // Only continue simulation if parent state is not terminal
        if (mdp.isTerminal(parentSimulation.state)) {
            return parentSimulation
        }

        val state = mdp.transition(parentSimulation.state, parentAction)
        return SimulationState(node, parentSimulation.state, state, mdp.actions(state))
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
        return getNextOptimalActionInternal(root)!!
    }

    private fun getNextOptimalActionInternal(node: StateActionNode<TAction>?): TAction? {
        val optimalAction = node!!.children.maxByOrNull { c -> c.n }
        if (optimalAction != null){
            return optimalAction!!.parentAction ?: throw Exception("No action computed")
        } else {
            return null
        }

    }

    fun getOptimalHorizon(): List<TAction> {
        val optimalHorizonArr = mutableListOf<TAction>()
        var node = root

        while (node != null){
            val optimalAction = getNextOptimalActionInternal(node)
            if (optimalAction != null) {
                optimalHorizonArr.add(optimalAction)
            }
            node = node!!.children.maxByOrNull { c -> c.n }
        }

        return optimalHorizonArr
    }

//    fun displayOptimalPath() {
//        val bestNodes = stateNodes.groupBy { s -> s.maxReward }.maxByOrNull { kvp -> kvp.key }?.value
//
//        println("Best candidates")
//        for (n in bestNodes!!) {
//            println("$n, depth: ${n.depth}")
//        }
//
//        val bestNode = bestNodes?.minByOrNull { n -> n.depth }
//
//        displayNode(bestNode!!)
//    }

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

        if (node is StateActionNode<*>) {
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