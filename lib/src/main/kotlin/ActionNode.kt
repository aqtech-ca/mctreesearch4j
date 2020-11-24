internal class ActionNode<TAction, TState>(val action: TAction, parent: NodeBase) : Node<StateNode<TAction, TState>>(parent) {
    fun parentState() : StateNode<TAction, TState> {
        return parent as StateNode<TAction, TState>
    }

    override fun toString(): String {
        return "Action: $action"
    }
}