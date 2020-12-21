package Twenty48

import Twenty48.Game2048

// open class Game2048State(gameObject: Game2048) {
open class Game2048State(GamePosition: Game2048Position) {

    // val gameGrid: Array<Array<Int>>, val maxScore: Double
    val gameGrid = GamePosition.grid
    val score = gameGrid.mapNotNull{ it.max() }.max()

    override fun toString(): String {
        val strArray = gameGrid.map { "[" + it.joinToString(",") }
        return strArray.joinToString("], ") + "]"
    }
}