package Twenty48

import MDP

class Game2048MDP(): MDP<Game2048State, Game2048Action>() {


    val gameObject = Game2048()
    // val initialGrid = gameObject.grid.map{it.copyOf()}.toTypedArray().copyOf()
    // val currentGameState = Game2048State(initialGrid, gameObject.gameScore.toDouble())
    val initialGameState = Game2048State(gameObject.grid.map{it.copyOf()}.toTypedArray().copyOf() ) // Incorrect, the bug is the initial state is changing, it should remain fixed


    override fun initialState(): Game2048State {
        return this.initialGameState
    }

    override fun reward(previousState: Game2048State?, action: Game2048Action?, state: Game2048State): Double {
        // this.GameState.transitionAction(this.GameState.gameObject.manipulateGrid(this.grid, waitForValidInput()))
        return this.gameObject.gameScore.toDouble()
    }

    override fun transition(state: Game2048State, action: Game2048Action): Game2048State {
        // this.gameObject.grid = this.gameObject.spawnNumber(this.gameObject.grid)
        // this.gameObject.grid = this.gameObject.manipulateGrid(state.gameGrid, action.toString())
        this.gameObject.run2048Programatic(this.gameObject.grid, action.toString())
        // Fix issue with constant mutation, the state needs to be tied to the grid!!
        // val newGrid = this.gameObject.run2048Programatic(this.gameObject.grid, action.toString())

        // return Game2048State(newGrid, this.gameObject.gameScore.toDouble())
        // gridworld score also
        return Game2048State(gameObject.grid)
    }

    override fun actions(state: Game2048State): Iterable<Game2048Action> {
        return sequenceOf(Game2048Action.valueOf("up"), Game2048Action.valueOf("down"), Game2048Action.valueOf("left"), Game2048Action.valueOf("right")).asIterable()
    }

    override fun isTerminal(state: Game2048State): Boolean {
        return this.gameObject.isGridSolved(this.gameObject.grid) or this.gameObject.isGridFull(this.gameObject.grid)
    }


}