package Reversi

import StateNode
import StatefulSolver
import java.awt.Point

class ReversiSolver(initialState: ReversiState)
    : StatefulSolver<ReversiState, Point>(ReversiMDP(initialState), 2000, 1.4, 0.9, false) {

    override fun iterateStep() {
//        displayTree(4)
        super.iterateStep()
    }

    override fun selectNode(node: StateNode<ReversiState, Point>): StateNode<ReversiState, Point> {
        val adversarial = adversarialSelect(node)
        val greedy = super.selectNode(adversarial)

        return greedy
    }

    private fun adversarialSelect(node: StateNode<ReversiState, Point>): StateNode<ReversiState, Point> {
        // No point in running adversarial until a min threshold is met
        if (node.n < 10) {
            return node
        }

        return node.getChildren().map { c -> adversarialSelect(c) }.maxByOrNull { n -> adversarialUCT(n, node.depth % 2 == 0) }!!
    }

    override fun formatNode(node: StateNode<ReversiState, Point>): String {
        return "Depth: ${node.depth} Action: ${node.inducingAction}, Max Reward: ${"%.5f".format(node.maxReward)}"
    }

    private fun adversarialUCT(node: StateNode<ReversiState, Point>, max: Boolean): Double {
        val parentN = node.parent?.n ?: node.n
        var reward = node.reward
        if (!max) {
            reward = -reward
        }
        return  calculateUCT(parentN, node.n, reward, explorationConstant)
    }

    fun getMove() : Point {
        constructTree(5000)
        return extractOptimalAction()
    }
}