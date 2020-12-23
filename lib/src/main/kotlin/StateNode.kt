internal class StateNode<TAction, TState>(
        parent: NodeBase?,
        val state: TState,
        val validActions: List<TAction>,
        val isTerminal: Boolean) : Node<ActionNode<TAction, TState>>(parent) {

    fun parentAction() : ActionNode<TAction, TState>? {
        return if (parent == null)
            null
        else
            parent as ActionNode<TAction, TState>
    }

    override fun toString(): String {
        return "State: $state, Max Reward: ${"%.5f".format(maxReward)}"
    }
}