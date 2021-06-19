package ca.aqtech.mctreesearch4j

import kotlin.math.ln
import kotlin.math.sqrt

open class AdvancedStatelessSolver<StateType, ActionType> (
    mdp: MDP<StateType, ActionType>,
    simulationDepthLimit: Int,
    explorationConstant: Double,
    rewardDiscountFactor: Double,
    verbose: Boolean
) : StatelessSolver<StateType, ActionType>(mdp, simulationDepthLimit, explorationConstant, rewardDiscountFactor, verbose) {

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