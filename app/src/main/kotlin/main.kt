fun main() {
    println("hello world")

    val gridworld = GridworldMDP(
            10,
            10,
            listOf(GridworldReward(4, 3, -10.0),
                GridworldReward(4, 6, -5.0),
                GridworldReward(9, 3, 10.0),
                GridworldReward(8, 8, 3.0)),
            0.7
    )

    // solve
}
