package mctreesearch4j

class StateNode<TState, TAction>(
    parent: StateNode<TState, TAction>?,
    inducingAction: TAction?,
    val state: TState,
    val validActions: Collection<TAction>,
    val isTerminal: Boolean) : Node<TAction, StateNode<TState, TAction>>(parent, inducingAction) {

    private val children = mutableMapOf<TAction, MutableCollection<StateNode<TState, TAction>>>()

    override fun addChild(child: StateNode<TState, TAction>) {
        if (child.inducingAction == null) {
            throw Exception("Inducing action must be set on child")
        }

        val childrenList = children.getOrPut(child.inducingAction, { mutableListOf() })
        childrenList.add(child)
    }

    override fun getChildren(action: TAction?): Collection<StateNode<TState, TAction>> {
        return if (action == null) {
            children.values.flatten()
        }
        else {
            children[action] ?: listOf()
        }
    }

    override fun toString(): String {
        return "State: $state, Max Reward: ${"%.5f".format(maxReward)}"
    }

    fun exploredActions() : Collection<TAction> {
        return children.keys
    }
}