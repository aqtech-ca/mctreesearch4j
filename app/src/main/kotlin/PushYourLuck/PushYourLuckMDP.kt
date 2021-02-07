package PushYourLuck

import MDP

class PushYourLuckMDP(val nDice: Int, val nSides: Int): MDP<PushYourLuckState, PushYourLuckAction>(){

    val diceObject = DiceClass(nDice = nDice, nSides = nSides)
    var currentReward = 0.0

    override fun initialState(): PushYourLuckState {
        return PushYourLuckState(Array(nDice){ i -> Array(nSides){i -> false}.toMutableList() }.toMutableList())
    }

    override fun transition(state: PushYourLuckState, action: PushYourLuckAction) : PushYourLuckState {
        //- diceObject.roll() -> diceObject.marked_sides
        //- diceObject.cashOut() -> diceObject.marked_sides
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