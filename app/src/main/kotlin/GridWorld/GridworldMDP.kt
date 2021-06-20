package GridWorld

import ca.aqtech.mctreesearch4j.MDP

class GridworldMDP(
        private val xSize: Int,
        private val ySize: Int,
        private val rewards: List<GridworldReward>,
        private val transitionProbability: Double,
        private val startingLocation: GridworldState = GridworldState(0, 0, false)) : MDP<GridworldState, GridworldAction>() {
    private val _allActions = GridworldAction.values().toList()
    private val _rewardStates = mutableMapOf<GridworldState, GridworldState>()

    override fun initialState(): GridworldState {
        return startingLocation
    }

    override fun isTerminal(state: GridworldState): Boolean {
        return rewards.any { r -> r.equals(state)}
    }

    fun visualizeState() {
        val stateArray = Array(xSize) { Array(ySize){"-"}}
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
        for (r in rewards) {
            if (r.equals(state)) {
                return r.value
            }
        }

        return 0.0
    }

    override fun transition(state: GridworldState, action: GridworldAction): GridworldState {
        if (state.isTerminal) {
            return state
        }
        else if (rewards.any { r -> r.equals(state)}) {
            return _rewardStates.getOrDefault(state, GridworldState(state.x, state.y, true))
        }

        // if target is out of bounds, return current state
        val targetNeighbour = state.resolveNeighbour(action, xSize, ySize) ?:
            return state

        if (Math.random() < transitionProbability){
            return targetNeighbour
        } else {
            var nonTargetNeighbours : MutableList<GridworldState>? = null

            for (a in _allActions.minus(action)) {
                val possibleNeighbour = state.resolveNeighbour(a, xSize, ySize)
                if (possibleNeighbour != null) {
                    if (nonTargetNeighbours == null) {
                        nonTargetNeighbours = mutableListOf()
                    }
                    nonTargetNeighbours.add(possibleNeighbour)
                }
            }

            return nonTargetNeighbours?.random() ?: throw Exception("No valid neighbours exist")
        }
    }

    override fun actions(state: GridworldState): Collection<GridworldAction> {
        return _allActions.filter { a -> state.isNeighbourValid(a, xSize, ySize) }
    }

}