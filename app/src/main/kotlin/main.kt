import kotlin.random.Random

fun main() {
    val gridworld = GridworldMDP(
            4,
            4,
            listOf(
                    GridworldReward(3, 1, -0.5),
                    GridworldReward(1, 3, -0.5),
                    GridworldReward(3, 3, 1.0),
                    GridworldReward(1, 1, 0.3)),
            1.0,
            GridworldState(1, 0, false)

    )

    // solve
    val solver = MCTSSolver(
            gridworld,
            Random.Default,
            500,
            20,
            1.4,
            0.9,
            false
    )

    // solver.buildTree()

    // solver.displayTree()

    // solver.displayOptimalPath()

    gridworld.visualizeState()

}
