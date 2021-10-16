
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import TestStochasticMDP
import ca.aqtech.mctreesearch4j.ActionNode
import ca.aqtech.mctreesearch4j.GenericSolver
import TestStochasticAction
import TestStochasticState

class CoreLibraryTests {
    // Test Node Expand
    // var root = ActionNode<TestStochasticState, TestStochasticAction>(null, null)
    // Test Simple MDP Function

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

    // Testing the GenericSolver (Default)
    var testRoot = solver.root

    // Ensure State content is not mutated in transition
    @Test fun coreLibraryStateMutationCheck() {
        val testNode = solver.expand(testRoot)
        assertTrue(testNode.state.stateIndex is Int,  "Is integer, state content unmutated.")
    }

    // Test selection returns an action node.
    @Test fun coreLibraryTestSelection(): ActionNode<TestStochasticState, TestStochasticAction>  {
        val node = solver.select(testRoot)
        assertTrue(node is ActionNode<TestStochasticState, TestStochasticAction>,  "Test Search Tree Selection")
        return node
    }

    // Ensure expansion is working properly, and meta-info is transfered from state-to-state.
    @Test fun coreLibraryTestExpand(): ActionNode<TestStochasticState, TestStochasticAction> {
        var nextNode = solver.expand(testRoot)
        val iterC = (2..99).random()
        for (i in 1..iterC){
            nextNode = solver.expand(nextNode)
        }
        assertTrue(nextNode.state.counter == iterC + 1,  "Test Search Tree Expansion")
        return nextNode
    }

    // Test selection returns an action node.
    @Test fun coreLibraryTestSelection2(): ActionNode<TestStochasticState, TestStochasticAction> {
        val node = solver.select(coreLibraryTestExpand())
        assertTrue(node is ActionNode<TestStochasticState, TestStochasticAction>,  "Test Search Tree Selection")
        return node
    }

    // Test simulation returns a valid numeric reward
    @Test fun coreLibraryTestSimulation() {
        val rewardValue = solver.simulate(testRoot)
        assertTrue(rewardValue is Double,  "Test Simulation")
    }

    // Test backpropagation function increment
    @Test fun coreLibraryTestBackpropagation() {
        var node = testRoot
        val n1 = node.n
        solver.backpropagate(testRoot, 20.0)
        val n2 = node.n
        println("n2: " + n2.toString())
        println("n1: " + n1.toString())
        assertTrue(n2 > n1,  "Test Increment of Node Visits")
    }

    // Tests for the base Solver

    // Test all key mechanisms, run single iteration
    @Test fun coreLibraryTestSingleIteration() {
        solver.runTreeSearchIteration()
    }

    // Test MC Tree Search, test if counted n times properly
    @Test fun coreLibraryTestMCTS() {
        solver.runTreeSearch(99)
        val rootN = testRoot.n
        println("n root: "+ rootN.toString())

        var nextNodes = testRoot.getChildren() ?: return
        var n1 = testRoot.n
        while(!nextNodes.isEmpty()){
            var nextNode = nextNodes.random()
            var n2 = nextNode.n
            println("next node n: " + n2.toString())
            nextNodes = nextNode.getChildren()
            assertTrue(n2 < n1,  "Test Increment of Node Visits")
            n1 = n2
        }
    }
}