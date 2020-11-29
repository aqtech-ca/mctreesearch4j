import kotlin.random.Random
import GridWorldGridSolve

fun main() {

    val gw = GridWorldGridSolve(
        4,
        4,
        listOf(
            GridworldReward(3, 1, -0.5),
            GridworldReward(1, 3, -0.5),
            // GridworldReward(3, 3, 1.0),
            GridworldReward(1, 1, 0.3)
        ),
        1.0)

    gw.getWorldSolve()
    gw.visualizeWorldSolve()



}
