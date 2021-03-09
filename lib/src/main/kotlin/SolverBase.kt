import kotlin.math.ln
import kotlin.math.sqrt

abstract class SolverBase<TAction, TNode>(
    protected val verbose: Boolean,
    protected val explorationConstant: Double
) : ISolver<TAction> where TNode : Node<TAction, TNode> {

    protected abstract var _root: TNode

    abstract fun selectNode(node: TNode) : TNode
    abstract fun expandNode(node: TNode) : TNode
    abstract fun runSimulation(node: TNode) : Double
    abstract fun updateNode(node: TNode, reward: Double)

    open fun endTreeConstruction() : Boolean {
        return false
    }

    override fun constructTree(iterations: Int) {
        for (i in 0..iterations) {
            iterateStep()

            if (endTreeConstruction()) {
                break
            }
        }
    }

    override fun iterateStep() {
        traceln("")
        traceln("New iteration")
        traceln("=============")

        // Selection
        val best = selectNode(_root)

        if (verbose) {
            traceln("Selected:")
            displayNode(best)
        }

        // Expansion
        val expanded = expandNode(best)

        if (verbose) {
            traceln("Expanding:")
            displayNode(expanded)
        }

        // Simulation
        val simulatedReward = runSimulation(expanded)

        traceln("Simulated Reward: $simulatedReward")

        // Update
        updateNode(expanded, simulatedReward)
    }

    // Utilities

    open fun calculateUCT(node: TNode) : Double {
        val parentN = node.parent?.n ?: node.n
        return calculateUCT(parentN, node.n, node.reward, explorationConstant)
    }

    open fun calculateUCT(parentN: Int, n: Int, reward: Double, explorationConstant: Double) : Double{
        return reward/n + explorationConstant* sqrt(ln(parentN.toDouble())/n)
    }

    // Policy extraction

    override fun extractOptimalAction(): TAction {
        return extractOptimalAction(_root)!!
    }

    private fun extractOptimalAction(node: TNode?): TAction? {
        return node?.getChildren()?.maxByOrNull { c -> c.n }?.inducingAction
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

    private fun displayNode(node: TNode) {
        if (node.parent != null) {
            displayNode(node.parent)
        }

        if (node.depth > 0) {
            print(" ".repeat((node.depth - 1)*2) + " └")
        }

        println(node)
    }

    fun displayTree() {
        displayTree(_root, "")
    }

    private fun displayTree(node: TNode?, indent: String) {
        if (node == null) {
            return
        }

        if (node.depth > 3) {
            return
        }

        val line = StringBuilder()
                .append(indent)
                .append(" $node")
                .append(" (n: ${node.n}, reward: ${"%.5f".format(node.reward)}, UCT: ${"%.5f".format(calculateUCT(node))})")

        println(line.toString())

        val children = node.getChildren()

        if (!children.any()) {
            return
        }

        for (child in children.take(children.size - 1)) {
            displayTree(child, generateIndent(indent) + " ├")
        }
        displayTree(children.last(), generateIndent(indent) + " └")
    }

    private fun generateIndent(indent: String) : String {
        return indent.replace('├', '│').replace('└', ' ')
    }
}