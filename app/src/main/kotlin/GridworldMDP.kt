class GridworldMDP(val xSize: Int, val ySize: Int, val rewards: List<GridworldReward>, val transitionProbability: Double) : MDP<GridworldState, GridworldAction>() {
    override fun InitialState(): IDistribution<GridworldState> {
        return UniformDistribution(listOf(GridworldState(0, 0, false)))
    }

    override fun IsTerminal(state: GridworldState): Boolean {
        return state.isTerminal
    }

    override fun Reward(previousState: GridworldState, action: GridworldAction, state: GridworldState): Double {
        if (state.isTerminal) {
            return 0.0
        }

        var reward = 0.0
        for (r in rewards) {
            if (r.equals(state)) {
                reward += r.value
            }
        }

        return reward
    }

    override fun Transition(state: GridworldState, action: GridworldAction): IDistribution<GridworldState> {
        if (state.isTerminal) {
            return UniformDistribution(listOf(state))
        }
        else if (rewards.any { r -> r.equals(state)}) {
            return UniformDistribution(listOf(GridworldState(state.x, state.y, true)))
        }

        // if target is out of bounds, return current state
        val targetNeighbour = state.ResolveNeighbour(action, xSize, ySize) ?:
            return UniformDistribution(listOf(state))

        var allNeighbours = mutableListOf<ProbabilisticElement<GridworldState>>()

        for (a in GridworldAction.values().toList().minus(action)) {
            val possibleNeighbour = state.ResolveNeighbour(a, xSize, ySize)
            if (possibleNeighbour != null) {
                allNeighbours.add(ProbabilisticElement(possibleNeighbour, 0.0))
            }
        }

        // compute probability of going into non-target neighbour state
        val otherProbability = (1 - transitionProbability) / allNeighbours.size

        for (n in allNeighbours) {
            n.probability = otherProbability
        }

        // add targetone
        allNeighbours.add(ProbabilisticElement(targetNeighbour, transitionProbability))

        return SparseCategoricalDistribution(allNeighbours)
    }

}