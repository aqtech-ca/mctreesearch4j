package GridWorld

class GridworldState(x: Int, y: Int, val isTerminal: Boolean) : GridworldPosition(x, y) {
    fun isNeighbourValid(action: GridworldAction, xSize: Int, ySize: Int) : Boolean {
        return when (action) {
            GridworldAction.UP -> {
                y != ySize - 1
            }
            GridworldAction.RIGHT -> {
                x != xSize - 1
            }
            GridworldAction.DOWN -> {
                y != 0
            }
            GridworldAction.LEFT -> {
                x != 0
            }
        }
    }

    fun resolveNeighbour(action: GridworldAction, xSize: Int, ySize: Int) : GridworldState? {
        return when (action) {
            GridworldAction.UP -> {
                if (y == ySize - 1)  null else GridworldState(x, y + 1, false)
            }
            GridworldAction.RIGHT -> {
                if (x == xSize - 1)  null else GridworldState(x + 1, y, false)
            }
            GridworldAction.DOWN -> {
                if (y == 0)  null else GridworldState(x, y - 1, false)
            }
            GridworldAction.LEFT -> {
                if (x == 0)  null else GridworldState(x - 1, y, false)
            }
        }
    }
}
// Override equality