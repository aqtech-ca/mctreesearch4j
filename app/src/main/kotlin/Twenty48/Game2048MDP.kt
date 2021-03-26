package Twenty48

import MDP
import kotlin.math.log2

class Game2048MDP(val initialGameState: Game2048State ) : MDP<Game2048State, Game2048Action>() {

    /*
    val initialGameState = Game2048State(Game2048Position(arrayOf(
        arrayOf(0, 0, 0, 0),
        arrayOf(0, 0, 0, 0),
        arrayOf(0, 0, 0, 0),
        arrayOf(0, 0, 0, 0)
    )))
    */

    override fun initialState(): Game2048State {
        return  initialGameState
    }

    override fun reward(previousState: Game2048State?, action: Game2048Action?, state: Game2048State): Double {
        return log2(state.score!!.toDouble())
    }

    override fun transition(state: Game2048State, action: Game2048Action): Game2048State {
        return state.makeMove(action, state.gameGrid)
    }

    override fun actions(state: Game2048State): Iterable<Game2048Action> {
        return sequenceOf(Game2048Action.valueOf("up"),
            Game2048Action.valueOf("down"),
            Game2048Action.valueOf("left"),
            Game2048Action.valueOf("right")).asIterable()
    }

    override fun isTerminal(state: Game2048State): Boolean {
        val gameObject = Game2048Controller()
        val isSolvedGrid = gameObject.isGridSolved(state.gameGrid )
        val isFullGrid = gameObject.isGridFull(state.gameGrid)
        return isSolvedGrid or isFullGrid
    }
}