package Mcts

//Defunct

abstract class POMDP<TState, TAction, TObservation> {
    abstract fun Transition(state: TState, action: TAction) : IDistribution<TState>

    abstract fun Observation(previousState: TState, action: TAction, state: TState) : IDistribution<TObservation>

    abstract fun Reward(previousState: TState, action: TAction, state: TState, observation: TObservation) : Double

    abstract fun InitialState() : IDistribution<TState>
}