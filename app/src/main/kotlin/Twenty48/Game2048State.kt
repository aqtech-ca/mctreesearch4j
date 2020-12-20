package Twenty48

import Twenty48.Game2048

open class Game2048State(val gameGrid: Array<Array<Int>>, val maxScore: Double ) {
    override fun toString(): String {
        val strArray = gameGrid.map { "[" + it.joinToString(",") }
        return strArray.joinToString("], ") + "]"
    }
}