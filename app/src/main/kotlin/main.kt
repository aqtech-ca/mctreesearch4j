import kotlin.random.Random
import GridWorldGridSolve

fun main() {

    val gw = GridWorldGridSolve(
        8,
        5,
        listOf(
            GridworldReward(5, 4, -10.0),
            // GridworldReward(3, 3, 1.0),
            GridworldReward(1, 1, 1000.0)
        ),
        1.0)

    gw.getWorldSolve()
    gw.visualizeWorldSolve()
}
