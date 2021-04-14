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

    var solver = ExtendedStatelessSolver(
            pylMDP,
            99,
            0.05,
            0.6,
            false
    )


    solver.constructTree(50)
    solver.displayTree()
    val optimalHorizon = solver.getOptimalHorizon()
    println(optimalHorizon.toString())
    println(optimalHorizon.size)

    // Write data
    val path = System.getProperty("user.dir")
    println("Working Directory = $path")

    /*
    val fileName = "outputs/pyl_output.txt"
    val outputFile = File(fileName)

    outputFile.printWriter().use { out ->
        out.println(solver.explorationTermHistory.joinToString(", "))
    }
     */

    File("outputs/gPYL_output_allExplorationTermHistory.txt").printWriter().use { out -> out.println(solver.allExplorationTermHistory.joinToString(", ")) }
    File("outputs/gPYL_output_allRewardsHistory.txt").printWriter().use { out -> out.println(solver.allRewardsHistory.joinToString(", ")) }
    File("outputs/gPYL_output_allActions.txt").printWriter().use { out -> out.println(solver.allActions.joinToString(", ")) }
    File("outputs/gPYL_output_optimalActionId.txt").printWriter().use { out -> out.println(solver.optimalActionId.joinToString(", ")) }
    File("outputs/gPYL_output_childNCount.txt").printWriter().use { out -> out.println(solver.childNCount.joinToString(", ")) }

}