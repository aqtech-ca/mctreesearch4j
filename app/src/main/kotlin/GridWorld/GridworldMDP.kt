package GridWorld

import MDP
import java.lang.Math

class GridworldMDP(
        private val xSize: Int,
        private val ySize: Int,
        private val rewards: List<GridworldReward>,
        private val transitionProbability: Double,
        private val startingLocation: GridworldState = GridworldState(0, 0, false)) : MDP<GridworldState, GridworldAction>() {
    override fun initialState(): GridworldState {
        return startingLocation
    }

    var lastAction: GridworldAction? = null

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
        val maxManhattan = xSize * ySize

        // Heuristic formula
        var manhattanSum = 0.0
        for (r in rewards) {
            if (r.equals(state)) {
                reward += r.value
            }
            if (r.value != 0.0){
                // Adjust to be scaled to the max distance.
                manhattanSum += r.value * (1 - (Math.abs(r.x - state.x) + Math.abs(r.y - state.y) / maxManhattan))
            }
        }
        reward += manhattanSum
        return reward
    }

    override fun transition(state: GridworldState, action: GridworldAction): GridworldState {
        lastAction = action

        if (state.isTerminal) {
            return state
        }
        else if (rewards.any { r -> r.equals(state)}) {
            return GridworldState(state.x, state.y, true)
        }

        // if target is out of bounds, return current state
        val targetNeighbour = state.ResolveNeighbour(action, xSize, ySize) ?:
            return state

        val nonTargetNeighbours = mutableListOf<GridworldState>()

        for (a in GridworldAction.values().toList().minus(action)) {
            val possibleNeighbour = state.ResolveNeighbour(a, xSize, ySize)
            if (possibleNeighbour != null) {
                nonTargetNeighbours.add(possibleNeighbour)
            }
        }

        if (Math.random() < transitionProbability){
            return targetNeighbour
        } else {
            return nonTargetNeighbours.random()
        }

    }

    override fun actions(state: GridworldState): Iterable<GridworldAction> {
        // Heuristic, don't go backwards

        return if (lastAction != null) {
            GridworldAction.values().filter { a -> state.ResolveNeighbour(a, xSize, ySize) != null && a != mirrorAction(lastAction!!) }
        } else {
            GridworldAction.values().filter { a -> state.ResolveNeighbour(a, xSize, ySize) != null }
        }

        // return legalActions.filter { a -> state.ResolveNeighbour(a, xSize, ySize) != null }
    }

    fun mirrorAction(action: GridworldAction): GridworldAction {
        return when(action) {
            GridworldAction.LEFT -> GridworldAction.RIGHT
            GridworldAction.RIGHT -> GridworldAction.LEFT
            GridworldAction.UP -> GridworldAction.DOWN
            GridworldAction.DOWN -> GridworldAction.UP
        }
    }

}