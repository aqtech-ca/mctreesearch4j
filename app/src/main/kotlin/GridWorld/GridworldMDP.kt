package GridWorld

import Mcts.MDP

class GridworldMDP(val xSize: Int,
                   val ySize: Int,
                   val rewards: List<GridworldReward>,
                   val transitionProbability: Double,
                   val startingLocation: GridworldState = GridworldState(
    0,
    0,
    false
)
)  : MDP<GridworldState, GridworldAction>() {

    override fun initialState(): GridworldState {
        return startingLocation
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

        var allNeighbours = mutableListOf<GridworldState>()

        for (a in GridworldAction.values().toList().minus(action)) {
            val possibleNeighbour = state.ResolveNeighbour(a, xSize, ySize)
            if (possibleNeighbour != null) {
                allNeighbours.add(possibleNeighbour)
            }
        }

        // compute probability of going into non-target neighbour state
        // val otherProbability = (1 - transitionProbability) / allNeighbours.size

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