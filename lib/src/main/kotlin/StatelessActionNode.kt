class StatelessActionNode<TState, TAction>(
        parent: StatelessActionNode<TState, TAction>?,
        inducingAction: TAction?) : Node<TAction, StatelessActionNode<TState, TAction>>(parent, inducingAction) {

    private var _state: TState? = null
    private var _validActions: Iterable<TAction>? = null

    var state: TState
        get() {
            return _state ?: throw Exception("Simulation not run at depth: $depth")
        }
        set(value) {
            _state = value
        }

    var validActions: Iterable<TAction>
        get() {
            return _validActions ?: throw Exception("Simulation not run")
        }
        set(value) {
            _validActions = value
        }

    private var children = mutableListOf<StatelessActionNode<TState, TAction>>()

    override fun addChild(child: StatelessActionNode<TState, TAction>) {
        children.add(child)
    }

    override fun getChildren(action: TAction?): Collection<StatelessActionNode<TState, TAction>> {
        return if (action == null) {
            children
        }
        else {
            children.filter { c -> c.inducingAction == action }
        }
    }

    override fun toString(): String {
        return "Action: $inducingAction, Max Reward: ${"%.5f".format(maxReward)}"
    }
}