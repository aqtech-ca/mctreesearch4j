internal open class NodeBase(val parent: NodeBase?) {
    val depth: Int
    var n = 0
    var reward = 0.0

    init {
        depth = if (parent == null) {
            0
        }
        else {
            parent.depth + 1
        }
    }
}