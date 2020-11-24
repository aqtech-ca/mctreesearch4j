internal class StateNode<TAction, TState>(val state: TState, parent: NodeBase?) : Node<ActionNode<TAction, TState>>(parent) {
    var validActions : List<TAction>? = null

    fun parentAction() : ActionNode<TAction, TState>? {
        return if (parent == null)
            null
        else
            parent as ActionNode<TAction, TState>
    }

    override fun toString(): String {
        return "State: $state"
    }
}