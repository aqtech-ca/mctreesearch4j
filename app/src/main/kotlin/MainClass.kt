

import GridWorld.GridWorldGridSolve
import GridWorld.GridworldReward
import Twenty48.Game2048

fun main() {

    /*
    val gw = GridWorldGridSolve(
        8,
        5,
        listOf(
            GridworldReward(5, 4, -0.5),
            // GridWorld.GridworldReward(3, 3, 1.0),
            GridworldReward(1, 1, 1.0)
        ),
        0.85
    )

    gw.getWorldSolve()
    gw.visualizeWorldSolve()
    */

    Game2048().main()

}
