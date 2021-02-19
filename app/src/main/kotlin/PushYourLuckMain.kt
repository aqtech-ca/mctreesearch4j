import PushYourLuck.PushYourLuckMDP
import kotlin.random.Random
import StatelessSolver
import java.io.File


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
            999,
            88,
            0.5,
            0.99,
            false
    )
    var rewardTracker = solver.buildTree()
    solver.displayTree()

    val optimalHorizon = solver.getOptimalHorizon()
    println(optimalHorizon.toString())
    println(optimalHorizon.size)
    print(rewardTracker)


    // val outputFileName = "outputs/PYL_rewards${iters.toString()}.txt"
    // Runtime.getRuntime().exec("touch $outputFileName")
    // File(outputFileName).writeText(rewardTracker.joinToString(","))

}