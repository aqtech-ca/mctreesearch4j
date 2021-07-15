package ca.aqtech.mctreesearch4j

class StateNode<StateType, ActionType> (
    parent: StateNode<StateType, ActionType>?,
    inducingAction: ActionType?,
    val state: StateType,
    val validActions: Collection<ActionType>,
    val isTerminal: Boolean
) : Node<ActionType, StateNode<StateType, ActionType>>(parent, inducingAction) {

    private val children = mutableMapOf<ActionType, StateNode<StateType, ActionType>>()

    override fun addChild(child: StateNode<StateType, ActionType>) {
        if (child.inducingAction == null) {
            throw Exception("Inducing action must be set on child")
        }
        if (children.keys.contains(child.inducingAction)) {
            throw Exception("A child with the same inducing action has already been added")
        }

        children[child.inducingAction] = child
    }

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

    override fun toString(): String {
        return "State: $state, Max Reward: ${"%.5f".format(maxReward)}"
    }

    fun exploredActions(): Collection<ActionType> {
        return children.keys
    }
}