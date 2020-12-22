package Mcts

internal class ActionNode<TAction, TState>(parent: NodeBase, val action: TAction) : Node<StateNode<TAction, TState>>(parent) {
    fun parentState() : StateNode<TAction, TState> {
        return parent as StateNode<TAction, TState>
    }

    override fun toString(): String {
        return "Action: $action"
    }

    fun toStringId(): String {
        return "$action"
    }
}