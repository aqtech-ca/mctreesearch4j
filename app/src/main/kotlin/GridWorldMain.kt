import GridWorld.GridWorldGridSolve
import GridWorld.GridworldMDP
import GridWorld.GridworldReward
import GridWorld.GridworldState
import kotlin.random.Random

fun main() {

    val setTransitionProbability = 0.8
    val setRewards = listOf(
            GridworldReward(5, 4, -0.5),
            // GridworldReward(3, 3, 1.0),
            GridworldReward(1, 1, 1.0)
    )

    val gw = GridWorldGridSolve(
            8,
            5,
            setRewards,
            0.85
    )

    gw.getWorldSolve()
    gw.visualizeWorldSolve()

    // Solve single
    var gridworld = GridworldMDP(
            xSize = 8,
            ySize = 5,
            rewards = setRewards,
            transitionProbability = 0.8,
            startingLocation = GridworldState(2, 2, false)
    )

    var gwSolver = StatelessSolver(
            gridworld,
            Random,
            999,
            50,
            1.4,
            0.9,
            false
    )
    val rewardsTracker = gwSolver.buildTree()
    gwSolver.displayTree()

    print(rewardsTracker)

}
