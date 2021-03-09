abstract class Node<TAction, TSelf>(
    val parent: TSelf?,
    val inducingAction: TAction?
) where TSelf : Node<TAction, TSelf> {
    val depth: Int = if (parent == null) {
        0
    }
    else {
        parent.depth + 1
    }
    var n = 0
    var reward = 0.0
    var maxReward = 0.0

    abstract fun addChild(child: TSelf)
    abstract fun getChildren(action: TAction? = null) : Collection<TSelf>
}