import GridWorld.GridWorldGridSolve
import GridWorld.GridworldReward
import GridWorld.GridworldState
import kotlin.random.Random

fun main() {
    /*
    val gwRewards = listOf(
        GridworldReward(5, 4, -0.5),
        // GridWorld.GridworldReward(3, 3, 1.0),
        GridworldReward(1, 1, 1.0)
    )
    var gridworld = GridWorld.GridworldMDP(
        8,
        5,
        gwRewards,
        0.85,
        GridworldState(0, 0, false)
    )

    var solver = MCTSSolver(
        gridworld,
        Random,
        200,
        40,
        1.4,
        0.9,
        true
    )
    solver.buildTree()
    solver.displayTree()
    */

    val gw = GridWorldGridSolve(
        8,
        5,
        listOf(
            GridworldReward(5, 4, -0.5),
            // GridworldReward(3, 3, 1.0),
            GridworldReward(1, 1, 1.0)
        ),
        0.85
    )

    gw.getWorldSolve()
    gw.visualizeWorldSolve()
}