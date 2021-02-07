import PushYourLuck.PushYourLuckMDP
import kotlin.random.Random
import StatelessSolver


fun main() {
    /*
    val dice = DiceClass(3, 6)

    println(dice.diceConfig)
    println(dice.markedSides.toString())

    for (i in 0 until 50){
        dice.roll()
        println(dice.diceConfig)
        println(dice.markedSides.toString())
    }

    dice.cashOut()
    print(dice.cumReward)

     */

    val pylMDP = PushYourLuckMDP(nDice = 3, nSides = 6)

    var solver = StatelessSolver(
            pylMDP,
            Random,
            49,
            50,
            1.4,
            0.9,
            true
    )
    solver.buildTree()
    solver.displayTree()
}