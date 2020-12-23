internal class StateActionNode<TAction>(
        parent: NodeBase?,
        val parentAction: TAction?) : NodeBase(parent) {

    var children = mutableListOf<StateActionNode<TAction>>()

    fun parentStateAction() : StateActionNode<TAction>? {
        return if (parent == null)
            null
        else
            parent as StateActionNode<TAction>
    }

    override fun toString(): String {
        return "Action: $parentAction, Max Reward: ${"%.5f".format(maxReward)}"
    }
}