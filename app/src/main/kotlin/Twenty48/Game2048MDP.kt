package Twenty48

import MDP

class Game2048MDP(): MDP<Game2048State, Game2048Action>() {

    private val gameObject = Game2048Controller() // We just use this for its methods

    val initialGrid = arrayOf(
        arrayOf(0, 0, 0, 0),
        arrayOf(0, 0, 0, 0),
        arrayOf(0, 0, 0, 0),
        arrayOf(0, 0, 0, 0)
    )
    val initialGamePosition = Game2048Position(initialGrid)
    var initialGameState = Game2048State(initialGamePosition)

    override fun initialState(): Game2048State {
        return initialGameState // The MCTS solver is mutating the initial game state??
    }

    override fun reward(previousState: Game2048State?, action: Game2048Action?, state: Game2048State): Double {
        return state.score!!.toDouble()
    }

    override fun transition(state: Game2048State, action: Game2048Action): Game2048State {

        var tempGrid = gameObject.manipulateGrid(state.gameGrid, action.toString()) // this code mutates initialGameState
        tempGrid = gameObject.spawnNumber(tempGrid)
        val newGamePosition = Game2048Position(tempGrid)

        this.initialGameState = Game2048State(Game2048Position(arrayOf(
            arrayOf(0, 0, 0, 0),
            arrayOf(0, 0, 0, 0),
            arrayOf(0, 0, 0, 0),
            arrayOf(0, 0, 0, 0)
        ))) // this is a bandaid over the initialState mutation issue

        return Game2048State(newGamePosition)
    }

    override fun actions(state: Game2048State): Iterable<Game2048Action> {
        return sequenceOf(Game2048Action.valueOf("up"),
            Game2048Action.valueOf("down"),
            Game2048Action.valueOf("left"),
            Game2048Action.valueOf("right")).asIterable()
    }

    override fun isTerminal(state: Game2048State): Boolean {
        val isSolvedGrid = gameObject.isGridSolved(state.gameGrid )
        val isFullGrid = gameObject.isGridFull(state.gameGrid)
        return isSolvedGrid or isFullGrid
    }
}