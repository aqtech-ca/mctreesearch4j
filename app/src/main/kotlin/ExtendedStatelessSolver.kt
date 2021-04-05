import kotlin.math.*

class ExtendedStatelessSolver<TState, TAction>(
        mdp: MDP<TState, TAction>,
        simulationDepthLimit: Int,
        explorationConstant: Double,
        rewardDiscountFactor: Double,
        verbose: Boolean
) : StatelessSolver<TState, TAction>(mdp, simulationDepthLimit, explorationConstant, rewardDiscountFactor, verbose) {

    val explorationTermHistory = mutableListOf<Double>()

    // Convergence on all exploration terms
    val allExplorationTermHistory = mutableListOf<List<Double>>()

    // Convergence on all rewards
    val allRewardsHistory = mutableListOf<List<Double>>()

    // Convergence on all visits
    val childNCount = mutableListOf<List<Int>>()

    // record id of all actions
    val allActions = mutableListOf<Iterable<TAction>>()

    // record of inducing optimal action
    val optimalActionId = mutableListOf<TAction>()

    override fun constructTree(iterations: Int) {
        for (i in 0..iterations) {
            iterateStep()

            val bestChild = root.getChildren().maxByOrNull { c -> calculateUCT(c)}

            println(root.getChildren().toString())
            println(bestChild)

            val ns = bestChild?.n ?: continue
            val explorationFactor = explorationConstant*sqrt(ln(i.toDouble())/ns)

            explorationTermHistory.add(explorationFactor)

            allExplorationTermHistory.add(root.getChildren().map{ child -> explorationConstant*sqrt( ln(i.toDouble())/child.n )  })
            allRewardsHistory.add(root.getChildren().map{ child -> child.reward /child.n })
            childNCount.add(root.getChildren().map{ child -> child.n })
            allActions.add(root.getChildren().map {child -> child.inducingAction!!})
            optimalActionId.add(bestChild.inducingAction!!)
        }
    }

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