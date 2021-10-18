enum class TestStochasticAction {
    LEFT, RIGHT;

    override fun toString(): String {
        return when (this) {
            LEFT -> "←"
            RIGHT -> "→"
        }
    }
}