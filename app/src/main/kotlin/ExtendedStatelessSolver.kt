import kotlin.math.ln
import kotlin.math.sqrt
import ca.aqtech.mctreesearch4j.MDP
import ca.aqtech.mctreesearch4j.SimpleSolver

class ExtendedStatelessSolver<StateType, ActionType>(
        mdp: MDP<StateType, ActionType>,
        simulationDepthLimit: Int,
        explorationConstant: Double,
        rewardDiscountFactor: Double,
        verbose: Boolean
) : SimpleSolver<StateType, ActionType>(mdp, simulationDepthLimit, explorationConstant, rewardDiscountFactor, verbose) {

    val explorationTermHistory = mutableListOf<Double>()

    override fun runTreeSearch(iterations: Int) {
        for (i in 0..iterations) {
            runTreeSearchIteration()

            val bestChild = root.getChildren().maxByOrNull { c -> calculateUCT(c)}

            println(root.getChildren().toString())
            println(bestChild)

            val ns = bestChild?.n ?: continue
            val explorationFactor = explorationConstant*sqrt(ln(i.toDouble())/ns)

            explorationTermHistory.add(explorationFactor)
        }
    }

    fun getOptimalHorizon(): List<ActionType> {
        val optimalHorizonArr = mutableListOf<ActionType>()
        var node = root

        while (true){
            node = node.getChildren().maxByOrNull { c -> c.n } ?: break
            optimalHorizonArr.add(node.inducingAction!!)
        }

        return optimalHorizonArr
    }
}