open class GridworldPosition(val x: Int, val y: Int) {
    override fun equals(other: Any?): Boolean {
        if (other is GridworldPosition) {
            return x == other.x && y == other.y
        }
        return super.equals(other)
    }
}