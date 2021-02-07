package PushYourLuck



import StatelessSolver
import kotlin.random.Random

class DiceClass(val nDice: Int,
                val nSides: Int,
                var markedSides: MutableList<MutableList<Boolean>> = Array(nDice){ i -> Array(nSides){i -> false}.toMutableList() }.toMutableList(),
                var cumReward: Double = 0.0){

    init {
        println("Initializing dice!")
        val diceConfig = Array(nDice){ i -> Array(nSides){i -> i + 1}.toList() }.toList()


        for (d in markedSides) {
            var rollInd = Random.nextInt(0, nSides-1)
            // d[rollInd] = !d[rollInd]
            d.set(rollInd, !d[rollInd])
        }


    }

    fun roll(): List<List<Boolean>> {
        // For each dice generate a random integer between (including) 0 to nSides-1
    }


}