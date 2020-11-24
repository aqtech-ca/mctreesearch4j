abstract class MDP<TState, TAction> {
    abstract fun transition(state: TState, action: TAction) : IDistribution<TState>

    abstract fun reward(previousState: TState?, action: TAction?, state: TState) : Double

    abstract fun initialState() : IDistribution<TState>

    abstract fun isTerminal(state: TState) : Boolean

    abstract fun actions(state: TState) : Iterable<TAction>
}