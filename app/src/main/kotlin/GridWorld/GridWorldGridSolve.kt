package GridWorld

import ca.aqtech.mctreesearch4j.SimpleSolver

class GridWorldGridSolve(
    private val xSize: Int,
    private val ySize: Int,
    private val rewards: List<GridworldReward>,
    private val transitionProbability: Double,
    private val iterations: Int = 1000,
    private val simDepth: Int = 40,
    private val exploreConstant: Double = 1.4,
    private val rewardDiscount: Double = 0.9,
    private val verbose: Boolean = false) {

    var mapOfSolutions = mutableMapOf<Pair<Int, Int>, String>()

    fun getWorldSolve() {

        val rewardLocations = mutableListOf<Pair<Int, Int>>()
        for (r in rewards) {
            rewardLocations.add( Pair(r.x, r.y))
        }

        for (x in 0 until xSize) {
            for (y in 0 until ySize) {
                if (Pair(x, y) !in rewardLocations){
                    val gridworld = GridworldMDP(
                        xSize,
                        ySize,
                        rewards,
                        transitionProbability,
                        GridworldState(x, y, false)
                    )

                    val solver = SimpleSolver(
                        gridworld,
                        iterations,
                        exploreConstant,
                        rewardDiscount,
                        verbose
                    )

                    // println("Solving at [$x, $y]")
                    solver.runTreeSearch(simDepth)
                    // solver.displayTree()
                    // println("Optimal action: ${solver.getNextOptimalAction()}")

                    mapOfSolutions[Pair(x, y)] = solver.extractOptimalAction().toString()
                }
            }
        }
    }

    fun visualizeWorldSolve() {

        for (y in ySize-1 downTo 0) {
            print('[')
            for (x in 0 until xSize) {
                val reward = rewards.singleOrNull { r -> r == GridworldPosition(x, y)}

                if (reward != null)
                {
                    if (reward.value > 0) {
                        print("R, ")
                    }
                    else {
                        print("X, ")
                    }
                }
                else {
                    print("${mapOfSolutions[Pair(x, y)]}, ")
                }
            }
            print(']')
            println()
        }
    }
}