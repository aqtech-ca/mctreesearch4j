package ca.aqtech.mctreesearch4j

/**
 * A representation of tree nodes used for Monte Carlo Tree Search (MCTS).
 *
 * This abstract type contains no logic but includes the definitions of functions that must be supported for each tree
 * node as well as commonly used values including depth, number of visits, current reward and the max reward among its
 * children.
 *
 * @param ActionType the type that represents the actions that can be taken in the MDP.
 * @param SelfType a convenience parameter to represent the type of itself.
 */
abstract class Node<ActionType, SelfType: Node<ActionType, SelfType>> (
    val parent: SelfType?,
    val inducingAction: ActionType?
) {
    /**
     * The depth of the node.
     */
    val depth: Int = if (parent == null) 0 else parent.depth + 1

    /**
     * The number of visits to the node.
     */
    var n = 0

    /**
     * The reward value of the node.
     */
    var reward = 0.0

    /**
     * The max reward value among the children of this node.
     */
    var maxReward = 0.0

    /**
     * Add a child to the current node.
     */
    abstract fun addChild(child: SelfType)

    /**
     * Get all the children of the current node.
     */
    abstract fun getChildren(action: ActionType? = null): Collection<SelfType>
}