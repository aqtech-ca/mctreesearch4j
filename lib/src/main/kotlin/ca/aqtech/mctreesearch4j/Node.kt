package ca.aqtech.mctreesearch4j

abstract class Node<ActionType, SelfType: Node<ActionType, SelfType>> (
    val parent: SelfType?,
    val inducingAction: ActionType?
) {
    val depth: Int = if (parent == null) 0 else parent.depth + 1
    var n = 0
    var reward = 0.0
    var maxReward = 0.0

    abstract fun addChild(child: SelfType)
    abstract fun getChildren(action: ActionType? = null): Collection<SelfType>
}