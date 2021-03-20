import GridWorld.*
import java.io.File

fun main() {

    val setRewards = listOf(
        GridworldReward(5, 3, -0.1),
        GridworldReward(5, 2, -0.1),
        GridworldReward(5, 1, -0.1),
        GridworldReward(5, 0, -0.1),
        GridworldReward(1, 1, 1.0)
    )

    val gw = GridWorldGridSolve(
            8,
            5,
            setRewards,
            0.85
    )

    gw.getWorldSolve()
    gw.visualizeWorldSolve()

    /*

    // Solve single
    var gridworld = GridworldMDP(
            xSize = 8,
            ySize = 5,
            rewards = setRewards,
            transitionProbability = 0.8,
            startingLocation = GridworldState(2, 2, false)
    )

    var gwSolver = ExtendedStatelessSolver(
            gridworld,
            999,
            1.4,
            0.9,
            false
    )
    gwSolver.constructTree(50)
    gwSolver.displayTree()

    // Write data
    val path = System.getProperty("user.dir")
    println("Working Directory = $path")

    val fileName = "outputs/gw_output.txt"
    val outputFile = File(fileName)

    outputFile.printWriter().use { out ->
        out.println(gwSolver.rewardHistory.joinToString(", "))
    }

     */

}
