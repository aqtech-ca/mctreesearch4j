import kotlin.math.*

class ExtendedStatelessSolver<TState, TAction>(
        mdp: MDP<TState, TAction>,
        simulationDepthLimit: Int,
        explorationConstant: Double,
        rewardDiscountFactor: Double,
        verbose: Boolean
) : StatelessSolver<TState, TAction>(mdp, simulationDepthLimit, explorationConstant, rewardDiscountFactor, verbose) {

    val rewardHistory = mutableListOf<Double>()

    override fun constructTree(iterations: Int) {
        for (i in 0..iterations) {
            iterateStep()

            val bestChild = _root.getChildren().maxByOrNull { c -> calculateUCT(c)}

            println(_root.getChildren().toString())
            println(bestChild)

            val ns = bestChild?.n ?: continue
            val explorationFactor = explorationConstant*sqrt(ln(i.toDouble())/ns)

            rewardHistory.add(explorationFactor)
        }
    }

    fun getOptimalHorizon(): List<TAction> {
        val optimalHorizonArr = mutableListOf<TAction>()
        var node = _root

        while (true){
            node = node.getChildren().maxByOrNull { c -> c.n } ?: break
            optimalHorizonArr.add(node.inducingAction!!)
        }

        return optimalHorizonArr
    }
}