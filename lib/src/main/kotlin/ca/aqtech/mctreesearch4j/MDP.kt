package ca.aqtech.mctreesearch4j

abstract class MDP<StateType, ActionType> {
    abstract fun transition(state: StateType, action: ActionType): StateType
    abstract fun reward(previousState: StateType?, action: ActionType?, state: StateType): Double
    abstract fun initialState(): StateType
    abstract fun isTerminal(state: StateType): Boolean
    abstract fun actions(state: StateType): Collection<ActionType>
}