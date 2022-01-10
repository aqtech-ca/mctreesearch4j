import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import ca.aqtech.mctreesearch4j.ActionNode
import ca.aqtech.mctreesearch4j.GenericSolver

/**
 * A set of unit tests to test the mctreesearch4j package. A simple MDP is designed [TestStochasticMDP].
 *
 * A set of tests is desigined for the [GenericSolver] and the base class [Solver]. The tests ensure proper implementation
 * of the vital classes.
 *
 */

class CoreLibraryTests {

    val testMDP = TestStochasticMDP()
    val depthLimit = 29
    val exploreConstant = 0.4
    val rewardDiscount = 0.01
    val verbose = false

    val solver = GenericSolver(
        testMDP,
        depthLimit,
        exploreConstant,
        rewardDiscount,
        verbose
    )

    var testRoot = solver.root

    // Generic Solver Testing
    /**
     * Tests the [GenericSolver.expand] method functionality without failure.
     */
    @Test fun coreLibraryTestExpandMethod() {
        solver.expand(testRoot)
    }

    /**
     * Tests the [GenericSolver.select] method functionality without failure.
     */
    @Test fun coreLibraryTestSelectMethod(): ActionNode<TestStochasticState, TestStochasticAction>  {
        return solver.select(testRoot)
    }

    /**
     * Tests the [GenericSolver.simulate] method functionality without failure.
     */
    @Test fun coreLibraryTestSimulation() {
        solver.simulate(testRoot)
    }

    /**
     * Tests the both the solver [GenericSolver.expand] and [GenericSolver.simulate]
     * basic functionality working in unison.
     */
    @Test fun coreLibraryTestExpandAndSelect(): ActionNode<TestStochasticState, TestStochasticAction> {
        return solver.select(coreLibraryTestExpand())
    }

    /**
     * Tests to ensure the [GenericSolver.expand] method is working properly,
     * and correct meta-info is transfered from state-to-state.
     */
    @Test fun coreLibraryTestExpand(): ActionNode<TestStochasticState, TestStochasticAction> {
        var nextNode = solver.expand(testRoot)
        val iterC = (2..99).random()
        for (i in 1..iterC){
            nextNode = solver.expand(nextNode)
        }
        assertTrue(nextNode.state.counter == iterC + 1,  "Test Search Tree Expansion")
        return nextNode
    }

    /**
     * Tests to ensure the backpropagation method backpropagates values properly. In this test the monotonic relation of
     * n visits from child to parent is always maintained.
     */
    @Test fun coreLibraryTestBackpropagation() {
        var node = testRoot
        val n1 = node.n
        solver.backpropagate(testRoot, 20.0)
        val n2 = node.n
        // println("n2: " + n2.toString())
        // println("n1: " + n1.toString())
        assertTrue(n2 >= n1,  "Monotonic guarantee of n child <= n parent in single Backpropagate")
    }

    // Base Solver Testing
    /**
     * Test all key mechanisms, run single iteration without failure.
     */
    @Test fun coreLibraryTestSingleIteration() {
        solver.runTreeSearchIteration()
    }

    /**
     * Test to ensure the MCTS algorithm as a whole is running, and gurantees a monotonic relationship from child node
     * to parent note, where n child >= n parent for any random traversal down the tree. This is a gurantee for any
     * MCTS algorithm.
     */
    @Test fun coreLibraryTestMCTS() {
        solver.runTreeSearch(99)
        val rootN = testRoot.n
        println("n root: "+ rootN.toString())

        var nextNodes = testRoot.getChildren()
        var n1 = testRoot.n
        while(!nextNodes.isEmpty()){
            var nextNode = nextNodes.random()
            var n2 = nextNode.n
            // println("next node n: " + n2.toString())
            nextNodes = nextNode.getChildren()
            assertTrue(n2 < n1,  "Monotonic guarantee of n child <= n parent in MCTS")
            n1 = n2
        }
    }
}