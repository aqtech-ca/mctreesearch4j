package PushYourLuck

import StatelessSolver
import kotlin.random.Random

class DiceClass(val nDice: Int,
                val nSides: Int,
                var markedSides: MutableList<MutableList<Boolean>> = MutableList(nDice){MutableList(nSides){false}},
                var instantReward: Double = 0.0
                ){


    val diceConfig = Array(nDice){ i -> Array(nSides){i -> i + 1}.toList() }.toList()

    fun roll(): MutableList<MutableList<Boolean>> {
        // For each dice generate a random integer between (including) 0 to nSides-1
        var diceRollResults = mutableListOf<Int>()
        for (d in markedSides) {
            diceRollResults.add(Random.nextInt(0, nSides-1))
            // Use -1 to represent unmarking
        }


        for (d in markedSides.indices) {
            var rollInd = diceRollResults[d]
            if (diceRollResults.filter{it == diceRollResults[d]}.size > 1){
                markedSides[d].set(rollInd, false)
            } else {
                markedSides[d].set(rollInd, !markedSides[d][rollInd])
            }

        }
        return markedSides
    }

    fun cashOut(): Double {
        for (d_ind in diceConfig.indices){
            if (markedSides[d_ind].any{ it }){
                // logic ia any side of the dice is market, indicated by bool true
                var runningProduct = 1.0
                for (s_ind in diceConfig[d_ind].indices){
                    if (markedSides[d_ind][s_ind]){
                        runningProduct *= diceConfig[d_ind][s_ind]
                    }
                }
                instantReward = runningProduct
            }
        }
        // Reset the dice
        markedSides = MutableList(nDice){MutableList(nSides){false}}
        return this.instantReward
    }

}

