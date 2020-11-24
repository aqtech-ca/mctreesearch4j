import kotlin.random.Random

fun main() {
    val gridworld = GridworldMDP(
            5,
            5,
            listOf(GridworldReward(4, 2, -10.0),
                GridworldReward(2, 4, -5.0),
                GridworldReward(4, 4, 10.0),
                GridworldReward(2, 2, 3.0)),
            1.0
    )

    // solve
    val solver = MCTSSolver(
            gridworld,
            Random.Default,
            1000,
            40,
            2.0,
            0.7
    )

    solver.solve()

    solver.display()

    solver.displayOptimalPath()

//    solver.initialize()
//
//    for (i in 0..100) {
//        solver.iterateStep(true)
//        solver.display()
//    }
}
