package ca.aqtech.mctreesearch4j

/**
 * A representation of nodes used by the stateful [StatefulSolver] to solve a Markov Decision Process (MDP).
 *
 * This type contains several convenience properties for implementing a stateful MDP solver.
 *
 * @param StateType the type that represents the states of the MDP.
 * @param ActionType the type that represents the actions that can be taken in the MDP.
 *
 * The constructor takes in a [StateNode] that represents the parent node, an [ActionType] that represents the
 * action taken to transition to the current node, a [StateType] that represents the state at this node, a set of valid
 * actions that can be taken from this node and whether this node represents a terminal state
 */
class StateNode<StateType, ActionType> (
    parent: StateNode<StateType, ActionType>?,
    inducingAction: ActionType?,
    val state: StateType,
    val validActions: Collection<ActionType>,
    val isTerminal: Boolean
) : Node<ActionType, StateNode<StateType, ActionType>>(parent, inducingAction) {

    private val children = mutableMapOf<ActionType, StateNode<StateType, ActionType>>()

    // Inherited doc comments
    override fun addChild(child: StateNode<StateType, ActionType>) {
        if (child.inducingAction == null) {
            throw Exception("Inducing action must be set on child")
        }
        if (children.keys.contains(child.inducingAction)) {
            throw Exception("A child with the same inducing action has already been added")
        }

        children[child.inducingAction] = child
    }

    // Inherited doc comments
    override fun getChildren(action: ActionType?): Collection<StateNode<StateType, ActionType>> {
        return if (action == null) {
            children.values
        } else {
            val child = children[action]
            if (child == null) {
                listOf()
            } else {
                listOf(child)
            }
        }
    }

    // Inherited doc comments
    override fun toString(): String {
        return "State: $state, Max Reward: ${"%.5f".format(maxReward)}"
    }

    /**
     * Returns all actions that have been taken at least once from this node.
     */
    fun exploredActions(): Collection<ActionType> {
        return children.keys
    }
}