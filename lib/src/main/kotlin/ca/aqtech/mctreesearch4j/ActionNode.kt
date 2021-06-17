package ca.aqtech.mctreesearch4j

class ActionNode<StateType, ActionType> (
    parent: ActionNode<StateType, ActionType>?,
    inducingAction: ActionType?
) : Node<ActionType, ActionNode<StateType, ActionType>>(parent, inducingAction) {

    private var _state: StateType? = null
    private var _validActions: Iterable<ActionType>? = null

    var state: StateType
        get() {
            return _state ?: throw Exception("Simulation not run at depth: $depth")
        }
        set(value) {
            _state = value
        }

    var validActions: Iterable<ActionType>
        get() {
            return _validActions ?: throw Exception("Simulation not run")
        }
        set(value) {
            _validActions = value
        }

    private var children = mutableListOf<ActionNode<StateType, ActionType>>()

    override fun addChild(child: ActionNode<StateType, ActionType>) {
        children.add(child)
    }

    override fun getChildren(action: ActionType?): Collection<ActionNode<StateType, ActionType>> {
        return if (action == null) children else children.filter { c -> c.inducingAction == action }
    }

    override fun toString(): String {
        return "Action: $inducingAction, Max Reward: ${"%.5f".format(maxReward)}"
    }
}