package PushYourLuck

import MDP

class PushYourLuckMDP(val nDice: Int, val nSides: Int): MDP<PushYourLuckState, PushYourLuckAction>(){

    var diceObject = DiceClass(nDice = nDice, nSides = nSides)
    var currentReward = 0.0

    override fun initialState(): PushYourLuckState {
        return PushYourLuckState(diceObject.markedSides)
    }

    override fun transition(state: PushYourLuckState, action: PushYourLuckAction) : PushYourLuckState {

        if (action == PushYourLuckAction.ROLL){
            return PushYourLuckState(diceObject.roll())
        } else if (action == PushYourLuckAction.CASHOUT) {
            currentReward = diceObject.cashOut()
            return PushYourLuckState(diceObject.markedSides)
        } else {
            throw IllegalArgumentException("Expected one of [cashout, roll]")
        }
    }

    override fun reward(previousState: PushYourLuckState?, action: PushYourLuckAction?, state: PushYourLuckState) : Double {
        return currentReward
    }

    override fun isTerminal(state: PushYourLuckState) : Boolean {
        return false
    }

    override fun actions(state: PushYourLuckState) : Iterable<PushYourLuckAction> {
        return sequenceOf(PushYourLuckAction.ROLL, PushYourLuckAction.CASHOUT).asIterable()
    }
}