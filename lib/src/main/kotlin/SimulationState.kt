internal class SimulationState<TAction, TState>(
    val node: StateActionNode<TAction>,
    val previousState: TState?,
    val state: TState,
    val validActions: Iterable<TAction>) { }