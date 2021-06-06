package GridWorld

import ca.aqtech.mctreesearch4j.StatelessSolver
import ca.aqtech.mctreesearch4j.StatefulSolver

class GridWorldGridSolve(val xSize: Int,
                         val ySize: Int,
                         val rewards: List<GridworldReward>,
                         val transitionProbability: Double,
                         val mcIter: Int = 1000,
                         val simDepth: Int = 40,
                         val exploreConstant: Double = 1.4,
                         val rewardDiscount: Double = 0.9,
                         val verboseBool: Boolean = false) {

    var mapOfSolutions = mutableMapOf<Pair<Int, Int>, String>()

    fun getWorldSolve() {

        var rewardLocations = mutableListOf<Pair<Int, Int>>()
        for (r in rewards) {
            rewardLocations.add( Pair(r.x, r.y))
        }

        for (x in 0 until xSize) {
            for (y in 0 until ySize) {
                if (Pair(x, y) !in rewardLocations){
                    var gridworld = GridworldMDP(
                        xSize,
                        ySize,
                        rewards,
                        transitionProbability,
                        GridworldState(x, y, false)
                    )

                    var solver = StatefulSolver(
                        gridworld,
                        mcIter,
                        exploreConstant,
                        rewardDiscount,
                        verboseBool
                    )

                    // println("Solving at [$x, $y]")
                    solver.constructTree(simDepth)
                    // solver.displayTree()
                    // println("Optimal action: ${solver.getNextOptimalAction()}")

                    mapOfSolutions[Pair(x, y)] = solver.extractOptimalAction().toString()
                }
            }
        }
    }

    fun visualizeWorldSolve(): Unit {

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