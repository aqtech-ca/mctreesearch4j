import Twenty48.*
import java.io.File

fun main() {

    /*
    val initialGameState = Game2048State(Game2048Position(arrayOf(
            arrayOf(1024, 0, 0, 0),
            arrayOf(512, 512, 0, 0),
            arrayOf(0, 0, 0, 0),
            arrayOf(0, 0, 0, 0)
    )))
    */

    var testGrid = arrayOf(
            arrayOf(0, 0, 2, 0),
            arrayOf(2, 2, 0, 0),
            arrayOf(64, 64, 2, 2),
            arrayOf(128, 256, 512, 1024)
    )

    val initialGameState = Game2048State(Game2048Position(testGrid))

    val game2048MDP = Game2048MDP(initialGameState)


    var solver = ExtendedStatelessSolver(
        game2048MDP,
        999,
        1.4*5,
        0.9,
        true
    )
    solver.constructTree(999)
    solver.displayTree()

    println("optimalAction")
    println(solver.extractOptimalAction().toString())

    println("optimal Horizon")
    val solList = solver.getOptimalHorizon().map { it.toString() }
    println(solList)

    // simply replay the 2048 game using the solution
    var gc = Game2048Controller()

    for (a in solList){
        testGrid = gc.manipulateGrid(testGrid, a)
    }
    println(Game2048State(Game2048Position(testGrid)).toString())
    println(Game2048State(Game2048Position(testGrid)).score.toString())

    // Write data
    val path = System.getProperty("user.dir")
    println("Working Directory = $path")

    /*
    val fileName = "outputs/g2048_output.txt"
    val outputFile = File(fileName)

    outputFile.printWriter().use { out ->
        out.println(solver.explorationTermHistory.joinToString(", "))
    }
     */

    File("outputs/g2048_output_allExplorationTermHistory.txt").printWriter().use { out -> out.println(solver.allExplorationTermHistory.joinToString(", ")) }
    File("outputs/g2048_output_allRewardsHistory.txt").printWriter().use { out -> out.println(solver.allRewardsHistory.joinToString(", ")) }
    File("outputs/g2048_output_allActions.txt").printWriter().use { out -> out.println(solver.allActions.joinToString(", ")) }
    File("outputs/g2048_output_optimalActionId.txt").printWriter().use { out -> out.println(solver.optimalActionId.joinToString(", ")) }
    File("outputs/g2048_output_childNCount.txt").printWriter().use { out -> out.println(solver.childNCount.joinToString(", ")) }

}
