package Twenty48

import Twenty48.Game2048

open class Game2048State(gameObject: Game2048) {

    // val gameGrid: Array<Array<Int>>, val maxScore: Double
    val gameGrid = gameObject.grid
    val score = gameObject.gameScore.toDouble()

    override fun toString(): String {
        val strArray = gameGrid.map { "[" + it.joinToString(",") }
        return strArray.joinToString("], ") + "]"
    }
}