package PushYourLuck

import MDP

class PushYourLuckMDP(val nDice: Int, val nSides: Int): MDP<PushYourLuckState, PushYourLuckAction>(){

    var diceObject = DiceClass(nDice = nDice, nSides = nSides)

    override fun initialState(): PushYourLuckState {
        return PushYourLuckState(diceObject.markedSides)
    }

    override fun transition(state: PushYourLuckState, action: PushYourLuckAction) : PushYourLuckState {
        return when {
            action === PushYourLuckAction.ROLL -> PushYourLuckState(diceObject.roll())
            action === PushYourLuckAction.CASHOUT -> {diceObject.cashOut(); PushYourLuckState(diceObject.markedSides) }
            else -> throw IllegalArgumentException("Expected one of [cashout, roll]")
        }
    }

    override fun reward(previousState: PushYourLuckState?, action: PushYourLuckAction?, state: PushYourLuckState) : Double {
        return diceObject.instantReward
    }

    override fun isTerminal(state: PushYourLuckState) : Boolean {
        return false
    }

    override fun actions(state: PushYourLuckState) : Collection<PushYourLuckAction> {
        return sequenceOf(PushYourLuckAction.ROLL, PushYourLuckAction.CASHOUT).toList()
    }
}