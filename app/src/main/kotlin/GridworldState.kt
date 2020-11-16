class GridworldState(x: Int, y: Int, val isTerminal: Boolean) : GridworldPosition(x, y) {
    fun ResolveNeighbour(action: GridworldAction, xSize: Int, ySize: Int) : GridworldState? {
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