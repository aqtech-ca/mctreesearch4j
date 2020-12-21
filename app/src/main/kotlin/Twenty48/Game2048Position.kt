package Twenty48

open class Game2048Position(grid: Array<Array<Int>>) {
    val grid = grid
    override fun toString(): String {
        val strArray = grid.map { "[" + it.joinToString(",") }
        return strArray.joinToString("], ") + "]"
    }
}