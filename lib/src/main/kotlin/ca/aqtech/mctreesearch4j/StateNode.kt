package ca.aqtech.mctreesearch4j

class StateNode<TState, TAction> (
    parent: StateNode<TState, TAction>?,
    inducingAction: TAction?,
    val state: TState,
    val validActions: Collection<TAction>,
    val isTerminal: Boolean
) : Node<TAction, StateNode<TState, TAction>>(parent, inducingAction) {

    private val children = mutableMapOf<TAction, StateNode<TState, TAction>>()

    override fun addChild(child: StateNode<TState, TAction>) {
        if (child.inducingAction == null) {
            throw Exception("Inducing action must be set on child")
        }
        if (children.keys.contains(child.inducingAction)) {
            throw Exception("A child with the same inducing action has already been added")
        }

        children[child.inducingAction] = child
    }

    override fun getChildren(action: TAction?): Collection<StateNode<TState, TAction>> {
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

    fun exploredActions(): Collection<TAction> {
        return children.keys
    }
}