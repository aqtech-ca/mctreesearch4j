package ca.aqtech.mctreesearch4j

import kotlin.math.ln
import kotlin.math.sqrt

/**
 * A representation of Markov Decision Process (MDP) solvers using Monte Carlo Tree Search (MCTS) methods.
 *
 * This abstract type contains four functions that must be implemented for a valid solver. Additional utility functions
 * are provided for convenience but can be overriden. The derived types of this class are used by the user to perform
 * MCTS on a given MDP.
 *
 * @param ActionType the type that represents the actions that can be taken in the MDP.
 * @param NodeType the type that represents the nodes of the tree that represents the MDP.
 *
 * The constructor takes in a boolean flag for debugging and a exploration constant to be used when choosing nodes to
 * visit during selection.
 */
abstract class Solver<ActionType, NodeType: Node<ActionType, NodeType>> (
    protected val verbose: Boolean,
    protected val explorationConstant: Double
) {
    /**
     * the root node of the tree.
     */
    protected abstract var root: NodeType

    /**
     * Returns a leaf node in the tree given a starting node in the tree.
     */
    protected abstract fun select(node: NodeType): NodeType

    /**
     * Creates and returns a new child node given a leaf node.
     */
    protected abstract fun expand(node: NodeType): NodeType

    /**
     * Runs a simulation from the given leaf node and computes a score for the node.
     */
    protected abstract fun simulate(node: NodeType): Double

    /**
     * Propagates the reward for the given node to the root of the tree.
     */
    protected abstract fun backpropagate(node: NodeType, reward: Double)

    /**
     * Runs a given number of iterations of MCTS. The order is specified by [runTreeSearchIteration]
     */
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

    /**
     * Runs a single iterations of MCTS. The default implementation runs [select], [expand], [simulate], [backpropagate]
     * in sequence. This can be overridden to improve performance for specific problem domains.
     */
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

    /**
     * Calculates the UCT score of a given node in the tree.
     */
    protected fun calculateUCT(node: NodeType) : Double {
        val parentN = node.parent?.n ?: node.n
        return calculateUCT(parentN, node.n, node.reward, explorationConstant)
    }

    /**
     * Calculates the UCT score given specific input parameters for the parent, the nubmer of visits, the reward
     * and the exploration constant.
     */
    protected open fun calculateUCT(parentN: Int, n: Int, reward: Double, explorationConstant: Double): Double {
        return reward/n + explorationConstant*sqrt(ln(parentN.toDouble())/n)
    }

    /**
     * Returns the best action from the root by choosing the node with the highest amount of visits. This function
     * can be overriden to modify the behavior to improve performance for a specific problem domain.
     */
    open fun extractOptimalAction(): ActionType? {
        return root.getChildren().maxByOrNull { c -> c.n }?.inducingAction
    }

    // Debug and Diagnostics

    /**
     * Prints the string with a new line if verbose output is enabled.
     */
    protected fun traceln(string: String) {
        if (verbose) {
            println(string)
        }
    }

    /**
     * Prints the string if verbose output is enabled.
     */
    protected fun trace(string: String) {
        if (verbose) {
            print(string)
        }
    }

    /**
     * Formats a given node into a string.
     */
    protected open fun formatNode(node: NodeType): String {
        return node.toString()
    }

    /**
     * Prints the tree starting from the given node.
     */
    open fun displayNode(node: NodeType) {
        if (node.parent != null) {
            displayNode(node.parent)
        }

        if (node.depth > 0) {
            print(" ".repeat((node.depth - 1)*2) + " └")
        }

        println(formatNode(node))
    }

    /**
     * Prints the tree starting from the root up to a given depth.
     */
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
