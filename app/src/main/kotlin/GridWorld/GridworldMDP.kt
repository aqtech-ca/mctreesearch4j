package GridWorld

import MDP
import ProbabilisticElement


class GridworldMDP(
        private val xSize: Int,
        private val ySize: Int,
        private val rewards: List<GridworldReward>,
        private val transitionProbability: Double,
        private val startingLocation: GridworldState = GridworldState(0, 0, false))  : MDP<GridworldState, GridworldAction>() {
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
        var reward = 0.0
        for (r in rewards) {
            if (r.equals(state)) {
                reward += r.value
            }
        }

        return reward
    }

    override fun transition(state: GridworldState, action: GridworldAction): GridworldState {
        if (state.isTerminal) {
            return state
        }
        else if (rewards.any { r -> r.equals(state)}) {
            return GridworldState(state.x, state.y, true)
        }

        // if target is out of bounds, return current state
        val targetNeighbour = state.ResolveNeighbour(action, xSize, ySize) ?:
            return state

        val allNeighbours = mutableListOf<GridworldState>()

        for (a in GridworldAction.values().toList().minus(action)) {
            val possibleNeighbour = state.ResolveNeighbour(a, xSize, ySize)
            if (possibleNeighbour != null) {
                allNeighbours.add(possibleNeighbour)
            }
        }

        // compute probability of going into non-target neighbour state
        val otherProbability = (1 - transitionProbability) / allNeighbours.size

        // add target neighbour
        // allNeighbours.add(targetNeighbour, transitionProbability)

        if (Math.random() < transitionProbability){
            return targetNeighbour
        } else {
            return allNeighbours.random()
        }

    }

    override fun actions(state: GridworldState): Iterable<GridworldAction> {
        return GridworldAction.values().filter { a -> state.ResolveNeighbour(a, xSize, ySize) != null }
    }

}