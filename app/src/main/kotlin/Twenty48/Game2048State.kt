package Twenty48

import Twenty48.Game2048

// open class Game2048State(gameObject: Game2048) {
open class Game2048State(grid: Array<Array<Int>>) {

    // val gameGrid: Array<Array<Int>>, val maxScore: Double
    val gameGrid = grid
    val score = grid.mapNotNull{ it.max() }.max()

    override fun toString(): String {
        val strArray = gameGrid.map { "[" + it.joinToString(",") }
        return strArray.joinToString("], ") + "]"
    }
}