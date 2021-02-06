import GridWorld.GridWorldGridSolve
import GridWorld.GridworldReward
import GridWorld.GridworldState
import kotlin.random.Random
import GridWorldGridSolve

fun main() {

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