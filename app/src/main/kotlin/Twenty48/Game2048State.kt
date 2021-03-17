package Twenty48

open class Game2048State(gamePosition: Game2048Position) {
    private val gameObject = Game2048Controller() // We just use this for its methods

    val gameGrid = gamePosition.grid
    val score = gameGrid.mapNotNull{ it.max() }.max()

    fun makeMove(action: Game2048Action, gameGrid: Array<Array<Int>> = this.gameGrid): Game2048State {
        val newPosition = Game2048Position(gameObject.spawnNumber(gameObject.manipulateGrid(gameGrid, action.toString())))
        return Game2048State(newPosition)
    }

    override fun toString(): String {
        val strArray = gameGrid.map { "[" + it.joinToString(",") }
        return strArray.joinToString("], ") + "]"
    }
}