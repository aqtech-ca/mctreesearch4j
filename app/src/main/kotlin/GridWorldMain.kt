import GridWorld.*
import java.io.File
import kotlin.math.pow


fun main() {

    /*
    val t1 = System.nanoTime()

    val setRewards = listOf(
            GridworldReward(5, 4, -0.5),
            // GridworldReward(3, 3, 1.0),
            GridworldReward(1, 1, 1.0)
    )

    val gw = GridWorldGridSolve(
            8,
            5,
            setRewards,
            0.85,
            mcIter = 50000,
            simDepth = 2000
    )

    gw.getWorldSolve()
    gw.visualizeWorldSolve()

     */

    /*
    // Solve single
    var gridworld = GridworldMDP(
            xSize = 8,
            ySize = 5,
            rewards = setRewards,
            transitionProbability = 0.8,
            startingLocation = GridworldState(4, 4, false)
    )

    var gwSolver = ExtendedStatelessSolver(
            gridworld,
            999,
            0.28*10,
            0.95,
            false
    )
    gwSolver.constructTree(999)
    gwSolver.displayTree()

    // Write data
    val path = System.getProperty("user.dir")
    println("Working Directory = $path")

    val allActionsOutputFile = File("outputs/gw_output_all_actions.txt")

    File("outputs/gw_output_allExplorationTermHistory.txt").printWriter().use { out -> out.println(gwSolver.allExplorationTermHistory.joinToString(", ")) }
    File("outputs/gw_output_allRewardsHistory.txt").printWriter().use { out -> out.println(gwSolver.allRewardsHistory.joinToString(", ")) }
    File("outputs/gw_output_allActions.txt").printWriter().use { out -> out.println(gwSolver.allActions.joinToString(", ")) }
    File("outputs/gw_output_optimalActionId.txt").printWriter().use { out -> out.println(gwSolver.optimalActionId.joinToString(", ")) }
    File("outputs/gw_output_childNCount.txt").printWriter().use { out -> out.println(gwSolver.childNCount.joinToString(", ")) }
     */

    // Benchmarking

    // for (1 to 50 <-)

    val depthList = (0 .. 100).map {it * 100}
    val iterList = (0 .. 5).map {it * 10.toDouble().pow(it).toInt() }
    // println(depthList)
    // println(benchmarkRuntimeDepth(depthList))

    println(iterList)
    println(benchmarkRuntimeMCIter(iterList))

}

fun benchmarkRuntimeDepth(depthsList: List<Int>): List<Double> {
    var durationArray = emptyList<Double>()

    for (depth in depthsList) {

        val t1 = System.nanoTime()

        val setRewards = listOf(
            GridworldReward(5, 4, -0.5),
            GridworldReward(1, 1, 1.0)
        )

        val gw = GridWorldGridSolve(
            8,
            5,
            setRewards,
            0.85,
            mcIter = 999,
            simDepth = depth
        )

        gw.getWorldSolve()
        gw.visualizeWorldSolve()

        val duration = (System.nanoTime() - t1) / 1e9
        println("Runtime: ")
        println(duration)
        durationArray = durationArray + duration

    }
    return durationArray

}


fun benchmarkRuntimeMCIter(iterList: List<Int>): List<Double> {
    var durationArray = emptyList<Double>()

    for (iter in iterList) {

        val t1 = System.nanoTime()

        val setRewards = listOf(
            GridworldReward(5, 4, -0.5),
            GridworldReward(1, 1, 1.0)
        )

        val gw = GridWorldGridSolve(
            8,
            5,
            setRewards,
            0.85,
            mcIter = iter,
            simDepth = 20000
        )

        gw.getWorldSolve()
        gw.visualizeWorldSolve()

        val duration = (System.nanoTime() - t1) / 1e9
        println("Runtime: ")
        println(duration)
        durationArray = durationArray + duration

    }
    return durationArray

}