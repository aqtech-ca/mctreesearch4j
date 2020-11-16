abstract class MDP<TState, TAction> {
    abstract fun Transition(state: TState, action: TAction) : IDistribution<TState>

    abstract fun Reward(previousState: TState, action: TAction, state: TState) : Double

    abstract fun InitialState() : IDistribution<TState>

    abstract fun IsTerminal(state: TState) : Boolean
}