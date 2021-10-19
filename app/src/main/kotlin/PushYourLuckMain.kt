package PushYourLuck

import PushYourLuck.PushYourLuckMDP
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

    val pylMDP = PushYourLuckMDP(nDice = 1, nSides = 6)

    var solver = ExtendedSolver.ExtendedStatelessSolver(
            pylMDP,
            999,
            0.07,
            0.99,
            false
    )

    solver.runTreeSearch(999)
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
        out.println(solver.explorationTermHistory.joinToString(", "))
    }

}