package ca.aqtech.mctreesearch4j

import kotlin.math.ln
import kotlin.math.sqrt

open class AdvancedStatelessSolver<TState, TAction> (
    mdp: MDP<TState, TAction>,
    simulationDepthLimit: Int,
    explorationConstant: Double,
    rewardDiscountFactor: Double,
    verbose: Boolean
) : StatelessSolver<TState, TAction>(mdp, simulationDepthLimit, explorationConstant, rewardDiscountFactor, verbose) {

    fun getOptimalHorizon(): List<TAction> {
        val optimalHorizonArr = mutableListOf<TAction>()
        var node = root

        while (true){
            node = node.getChildren().maxByOrNull { c -> c.n } ?: break
            optimalHorizonArr.add(node.inducingAction!!)
        }

        return optimalHorizonArr
    }
}