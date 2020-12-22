package Mcts

internal open class Node<TChild>(parent: NodeBase?) : NodeBase(parent) where TChild : NodeBase {
    var children = mutableListOf<TChild>()
}