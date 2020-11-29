import GridworldMDP
import kotlin.random.Random

class GridWorldGridSolve(val xSize: Int, val ySize: Int, val rewards: List<GridworldReward>, val transitionProbability: Double) {

    fun getWorldSolve() {
        for (x in 0 until xSize) {
            for (y in 0 until ySize) {

            }
        }

        val gridworld = GridworldMDP(
            xSize,
            ySize,
            rewards,
            transitionProbability,
            GridworldState(2, 2, false)
        )

        // solve
        val solver = MCTSSolver(
            gridworld,
            Random.Default,
            1000,
            20,
            1.4,
            0.9,
            false
        )

        solver.buildTree()
        // solver.displayTree()
        // solver.displayOptimalPath()
        gridworld.visualizeState()
        println(solver.getNextOptimalAction())




    }


}