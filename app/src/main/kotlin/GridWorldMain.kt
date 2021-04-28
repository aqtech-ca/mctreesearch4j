import GridWorld.*
import java.io.File

fun main() {

    val setRewards = listOf(
            GridworldReward(5, 4, -0.5),
            // GridworldReward(3, 3, 1.0),
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
            0.28,
            0.95,
            false
    )
    gwSolver.constructTree(999)
    gwSolver.displayTree()

    // Write data
    val path = System.getProperty("user.dir")
    println("Working Directory = $path")

    val fileName = "outputs/gw_output.txt"
    val outputFile = File(fileName)

    outputFile.printWriter().use { out ->
        out.println(gwSolver.explorationTermHistory.joinToString(", "))
    }
}
