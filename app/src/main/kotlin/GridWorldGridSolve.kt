import GridworldMDP
import kotlin.random.Random

class GridWorldGridSolve(val xSize: Int, val ySize: Int, val rewards: List<GridworldReward>, val transitionProbability: Double) {



    var mapOfSolutions = mutableMapOf<Pair<Int, Int>, String>()
    val actionPrettyMap: Map<String, String> = mapOf("LEFT" to "←", "RIGHT" to "→", "UP" to "↑", "DOWN" to "↓")

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

                    var solver = MCTSSolver(
                        gridworld,
                        Random.Default,
                        500,
                        20,
                        0.3,
                        0.05,
                        false
                    )
                    solver.buildTree()
                    // solver.displayTree()
                    // solver.displayOptimalPath()
                    // gridworld.visualizeState()
                    // println(solver.getNextOptimalAction())
                    mapOfSolutions.put(Pair(x, y), actionPrettyMap.get(solver.getNextOptimalAction()).toString())
                }

            }
        }
        // println(mapOfSolutions)
    }
    fun visualizeWorldSolve(): Unit {

        var stateArray = Array(ySize) { Array(xSize) { "-" } }
        for ((p, action) in mapOfSolutions) {
            stateArray[p.second][p.first] = action
            for (r in rewards) {
                if (r.value > 0) stateArray[r.y][r.x] = "R"
                if (r.value < 0) stateArray[r.y][r.x] = "X"
            }
        }


        for (i in stateArray.indices) {
            println(stateArray[i].contentToString())
        }
    }
}