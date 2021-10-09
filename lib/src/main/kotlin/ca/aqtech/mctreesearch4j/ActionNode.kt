package ca.aqtech.mctreesearch4j

/**
 * A representation of nodes used by the stateless [GenericSolver] to solve a Markov Decision Process (MDP).
 *
 * This type contains several convenience properties for implementing a stateless MDP solver.
 *
 * @param StateType the type that represents the states of the MDP.
 * @param ActionType the type that represents the actions that can be taken in the MDP.
 *
 * The constructor takes in a [ActionNode] that represents the parent node and an [ActionType] that represents the
 * action taken to transition to the current node.
 */
class ActionNode<StateType, ActionType> (
    parent: ActionNode<StateType, ActionType>?,
    inducingAction: ActionType?
) : Node<ActionType, ActionNode<StateType, ActionType>>(parent, inducingAction) {

    private var _state: StateType? = null
    private var _validActions: Iterable<ActionType>? = null

    /**
     * The state at this node. This is only available if a simulation has run.
     */
    var state: StateType
        get() {
            return _state ?: throw Exception("Simulation not run at depth: $depth")
        }
        set(value) {
            _state = value
        }

    /**
     * A list of actions that can be taken from this node. This is only available if a simulation has run.
     */
    var validActions: Iterable<ActionType>
        get() {
            return _validActions ?: throw Exception("Simulation not run")
        }
        set(value) {
            _validActions = value
        }

    private var children = mutableListOf<ActionNode<StateType, ActionType>>()

    // Inherited doc comments
    override fun addChild(child: ActionNode<StateType, ActionType>) {
        children.add(child)
    }

    // Inherited doc comments
    override fun getChildren(action: ActionType?): Collection<ActionNode<StateType, ActionType>> {
        return if (action == null) children else children.filter { c -> c.inducingAction == action }
    }

    // Inherited doc comments
    override fun toString(): String {
        return "Action: $inducingAction, Max Reward: ${"%.5f".format(maxReward)}"
    }
}