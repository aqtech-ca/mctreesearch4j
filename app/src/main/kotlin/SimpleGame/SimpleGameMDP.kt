package SimpleGame

import MDP

class SimpleGameMDP(): MDP<SimpleGameState, SimpleGameAction>(){

    var mu1Var = 0.0

    override fun initialState(): SimpleGameState {
        return SimpleGameState()
    }

    override fun transition(state: SimpleGameState, action: SimpleGameAction) : SimpleGameState {
        return when {
            action === SimpleGameAction.ACTION1 -> {mu1Var += 0.5; SimpleGameState(mu1Var, 0.0)}
            action === SimpleGameAction.ACTION2 -> {SimpleGameState(mu1Var, 0.0) }
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