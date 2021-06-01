package mctreesearch4j

import kotlin.math.ln
import kotlin.math.sqrt

abstract class SolverBase<TAction, TNode>(
    protected val verbose: Boolean,
    protected val explorationConstant: Double
) where TNode : Node<TAction, TNode> {

    protected abstract var root: TNode

    abstract fun selectNode(node: TNode) : TNode
    abstract fun expandNode(node: TNode) : TNode
    abstract fun runSimulation(node: TNode) : Double
    abstract fun updateNode(node: TNode, reward: Double)

    open fun constructTree(iterations: Int) {
        for (i in 0..iterations) {
            if (verbose)
            {
                traceln("")
                traceln("New iteration $i")
                traceln("=============")
            }
            iterateStep()
        }
    }

    open fun iterateStep() {
        // Selection
        val best = selectNode(root)

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

    open fun extractOptimalAction(): TAction {
        return extractOptimalAction(root)!!
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

    protected open fun formatNode(node: TNode) : String {
        return node.toString()
    }

    fun displayNode(node: TNode) {
        if (node.parent != null) {
            displayNode(node.parent)
        }

        if (node.depth > 0) {
            print(" ".repeat((node.depth - 1)*2) + " └")
        }

        println(formatNode(node))
    }

    fun displayTree(depthLimit: Int = 3) {
        displayTree(depthLimit, root, "")
    }

    private fun displayTree(depthLimit: Int, node: TNode?, indent: String) {
        if (node == null) {
            return
        }

        if (node.depth > depthLimit) {
            return
        }

        val line = StringBuilder()
                .append(indent)
                .append(" ${formatNode(node)}")
                .append(" (n: ${node.n}, reward: ${"%.5f".format(node.reward)}, UCT: ${"%.5f".format(calculateUCT(node))})")

        println(line.toString())

        val children = node.getChildren()

        if (!children.any()) {
            return
        }

        for (child in children.take(children.size - 1)) {
            displayTree(depthLimit, child, generateIndent(indent) + " ├")
        }
        displayTree(depthLimit, children.last(), generateIndent(indent) + " └")
    }

    private fun generateIndent(indent: String) : String {
        return indent.replace('├', '│').replace('└', ' ')
    }
}