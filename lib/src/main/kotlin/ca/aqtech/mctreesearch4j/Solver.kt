package ca.aqtech.mctreesearch4j

import kotlin.math.ln
import kotlin.math.sqrt

abstract class Solver<ActionType, NodeType: Node<ActionType, NodeType>> (
    protected val verbose: Boolean,
    protected val explorationConstant: Double
) {
    protected abstract var root: NodeType

    protected abstract fun select(node: NodeType): NodeType
    protected abstract fun expand(node: NodeType): NodeType
    protected abstract fun simulate(node: NodeType): Double
    protected abstract fun backpropagate(node: NodeType, reward: Double)

    open fun runTreeSearch(iterations: Int) {
        for (i in 0..iterations) {
            if (verbose)
            {
                traceln("")
                traceln("New iteration $i")
                traceln("=============")
            }
            runTreeSearchIteration()
        }
    }

    open fun runTreeSearchIteration() {
        // Selection
        val best = select(root)

        if (verbose) {
            traceln("Selected:")
            displayNode(best)
        }

        // Expansion
        val expanded = expand(best)

        if (verbose) {
            traceln("Expanding:")
            displayNode(expanded)
        }

        // Simulation
        val simulatedReward = simulate(expanded)

        traceln("Simulated Reward: $simulatedReward")

        // Update
        backpropagate(expanded, simulatedReward)
    }

    // Utilities

    protected fun calculateUCT(node: NodeType) : Double {
        val parentN = node.parent?.n ?: node.n
        return calculateUCT(parentN, node.n, node.reward, explorationConstant)
    }

    protected open fun calculateUCT(parentN: Int, n: Int, reward: Double, explorationConstant: Double): Double {
        return reward/n + explorationConstant*sqrt(ln(parentN.toDouble())/n)
    }

    open fun extractOptimalAction(): ActionType? {
        return root.getChildren().maxByOrNull { c -> c.n }?.inducingAction
    }

    // Debug and Diagnostics

    protected fun traceln(string: String) {
        if (verbose) {
            println(string)
        }
    }

    protected fun trace(string: String) {
        if (verbose) {
            print(string)
        }
    }

    protected open fun formatNode(node: NodeType): String {
        return node.toString()
    }

    open fun displayNode(node: NodeType) {
        if (node.parent != null) {
            displayNode(node.parent)
        }

        if (node.depth > 0) {
            print(" ".repeat((node.depth - 1)*2) + " └")
        }

        println(formatNode(node))
    }

    open fun displayTree(depthLimit: Int = 3) {
        displayTree(depthLimit, root, "")
    }

    private fun displayTree(depthLimit: Int, node: NodeType?, indent: String) {
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

    private fun generateIndent(indent: String): String {
        return indent.replace('├', '│').replace('└', ' ')
    }
}