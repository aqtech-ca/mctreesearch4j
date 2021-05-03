package Reversi

import mcts.StateNode
import mcts.StatelessActionNode
import java.awt.Point

class ReversiSolverMinimax(initialState: ReversiState)
    : ReversiSolverBase(initialState) {

    override fun iterateStep() {
//        displayTree(4)
        super.iterateStep()
    }

    override fun selectNode(node: StatelessActionNode<ReversiState, Point>): StatelessActionNode<ReversiState, Point> {
        val adversarial = adversarialSelect(node)
        return super.selectNode(adversarial)
    }

    private fun adversarialSelect(node: StatelessActionNode<ReversiState, Point>): StatelessActionNode<ReversiState, Point> {
        // No point in running adversarial until a min threshold is met
        if (node.n < 10 || node.depth > 4) {
            return node
        }

        return node.getChildren().map { c -> adversarialSelect(c) }.maxByOrNull { n -> adversarialUCT(n, node.depth % 2 == 0) } ?: node
    }

    private fun adversarialUCT(node: StatelessActionNode<ReversiState, Point>, max: Boolean): Double {
        val parentN = node.parent?.n ?: node.n
        var reward = node.reward
        if (!max) {
            reward = -reward
        }
        return  calculateUCT(parentN, node.n, reward, explorationConstant)
    }

    override fun formatNode(node: StatelessActionNode<ReversiState, Point>): String {
        return "Depth: ${node.depth} Action: ${node.inducingAction}, Max Reward: ${"%.5f".format(node.maxReward)}"
    }
}