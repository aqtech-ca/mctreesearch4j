package GridWorld

enum class GridworldAction {
    UP, DOWN, LEFT, RIGHT;

    override fun toString(): String {
        return when (this) {
            UP -> "↑"
            DOWN -> "↓"
            LEFT -> "←"
            RIGHT -> "→"
        }
    }
}
