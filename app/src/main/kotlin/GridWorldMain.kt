import GridWorld.GridWorldGridSolve
import GridWorld.GridworldMDP
import GridWorld.GridworldReward
import GridWorld.GridworldState
import java.io.File
import kotlin.random.Random

fun main() {

    val setRewards = listOf(
            GridworldReward(5, 4, -0.5),
            // GridworldReward(3, 3, 1.0),
            GridworldReward(1, 1, 1.0)
    )

    val worldFeaturesWall = listOf(
        GridworldReward(5, 3, -0.1),
        GridworldReward(5, 2, -0.1),
        GridworldReward(5, 1, -0.1),
        GridworldReward(5, 0, -0.1),
        GridworldReward(1, 1, 1.0)
    )

    val gw = GridWorldGridSolve(
            8,
            5,
            worldFeaturesWall,
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

    var gwSolver = StatelessSolver(
            gridworld,
            Random,
            999,
            50,
            1.4,
            0.9,
            false
    )
    val rewardTracker = gwSolver.buildTree()
    gwSolver.displayTree()

    // Write data
    val path = System.getProperty("user.dir")
    println("Working Directory = $path")

    val fileName = "outputs/gw_output.txt"
    val outputFile = File(fileName)

    outputFile.printWriter().use { out ->
        out.println(rewardTracker.joinToString(", "))
    }

     */

}
