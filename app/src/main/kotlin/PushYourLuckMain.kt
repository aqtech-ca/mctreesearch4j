import PushYourLuck.PushYourLuckMDP
import kotlin.random.Random
import StatelessSolver
import java.io.File
import kotlin.math.ln
import kotlin.math.sqrt


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

    val pylMDP = PushYourLuckMDP(nDice = 1, nSides = 6)

    var solver = StatelessSolver(
            pylMDP,
            Random,
            99,
            45,
            1.7,
            0.99,
            false
    )

    var rewardTracker = solver.buildTree()
    solver.displayTree()
    val optimalHorizon = solver.getOptimalHorizon()
    println(optimalHorizon.toString())
    println(optimalHorizon.size)

    // Write data
    val path = System.getProperty("user.dir")
    println("Working Directory = $path")

    val fileName = "outputs/pyl_output.txt"
    val outputFile = File(fileName)

    outputFile.printWriter().use { out ->
        out.println(rewardTracker.joinToString(", "))
    }

}