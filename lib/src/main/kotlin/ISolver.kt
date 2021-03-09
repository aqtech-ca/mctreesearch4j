interface ISolver<TAction> {
    // Construct the MCTS tree using a number of iterations
    fun constructTree(iterations: Int)

    fun iterateStep()

    // Extract the best action using the constructed tree
    fun extractOptimalAction(): TAction
}