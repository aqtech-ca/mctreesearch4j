package SimpleGame

import MDP

class SimpleGameMDP(): MDP<SimpleGameState, SimpleGameAction>(){

    override fun initialState(): SimpleGameState {
        return SimpleGameState()
    }

    override fun transition(state: SimpleGameState, action: SimpleGameAction) : SimpleGameState {
        return when {
            action === SimpleGameAction.ACTION1 -> {SimpleGameState(state.mu1 + 0.5, state.mu2)}
            action === SimpleGameAction.ACTION2 -> {SimpleGameState(state.mu1, state.mu2) }
            else -> throw IllegalArgumentException("Expected one of [ACTION1, ACTION2]")
        }
    }

    override fun reward(previousState: SimpleGameState?, action: SimpleGameAction?, state: SimpleGameState) : Double {
        return when {
            action != null -> state.getReward(action)
            else -> 0.0
        }
    }

    override fun isTerminal(state: SimpleGameState) : Boolean {
        return false
    }

    override fun actions(state: SimpleGameState) : Iterable<SimpleGameAction> {
        return sequenceOf(SimpleGameAction.ACTION1, SimpleGameAction.ACTION2).asIterable()
    }
}