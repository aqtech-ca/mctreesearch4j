import MCTSSolver

class GridworldMDP(val xSize: Int, val ySize: Int, val rewards: List<GridworldReward>, val transitionProbability: Double, val startingLocation: GridworldState = GridworldState(0, 0, false))  : MDP<GridworldState, GridworldAction>() {
    override fun initialState(): IDistribution<GridworldState> {
        return UniformDistribution(listOf(startingLocation))
    }

    override fun isTerminal(state: GridworldState): Boolean {
        return rewards.any { r -> r.equals(state)}
    }

    fun visualizeState(): Unit {
        var stateArray = Array(xSize) { Array(ySize){"-"}}
        stateArray[this.startingLocation.y][this.startingLocation.x] = "A"
        for (r in rewards) {
            if (r.value > 0) stateArray[r.y][r.x] = "*"
            if (r.value < 0) stateArray[r.y][r.x] = "X"
        }
        for (i in stateArray.size -1 downTo 0) {
            println(stateArray[i].contentToString())
        }
    }

    override fun reward(previousState: GridworldState?, action: GridworldAction?, state: GridworldState): Double {
        var reward = 0.0
        for (r in rewards) {
            if (r.equals(state)) {
                reward += r.value
            }
        }

        return reward
    }

    override fun transition(state: GridworldState, action: GridworldAction): IDistribution<GridworldState> {
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

        // add target neighbour
        allNeighbours.add(ProbabilisticElement(targetNeighbour, transitionProbability))

        return SparseCategoricalDistribution(allNeighbours)
    }

    override fun actions(state: GridworldState): Iterable<GridworldAction> {
        return GridworldAction.values().filter { a -> state.ResolveNeighbour(a, xSize, ySize) != null }
    }

}