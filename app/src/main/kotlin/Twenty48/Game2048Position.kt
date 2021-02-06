package Twenty48

open class Game2048Position(val grid: Array<Array<Int>>) {
    override fun toString(): String {
        val strArray = grid.map { "[" + it.joinToString(",") }
        return strArray.joinToString("], ") + "]"
    }
}