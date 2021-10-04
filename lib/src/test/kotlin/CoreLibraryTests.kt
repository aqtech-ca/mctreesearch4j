
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import TestStochasticMDP
import ca.aqtech.mctreesearch4j.ActionNode
import ca.aqtech.mctreesearch4j.GenericSolver


class CoreLibraryTests {
    // Test Node Expand
    // var root = ActionNode<TestStochasticState, TestStochasticAction>(null, null)
    // Test Simple MDP Function

    val testMDP = TestStochasticMDP()
    val iterations = 1
    val exploreConstant = 0.4
    val rewardDiscount = 0.01
    val verbose = true

    val solver = GenericSolver(
        testMDP,
        iterations,
        exploreConstant,
        rewardDiscount,
        verbose
    )

    var testRoot = solver.root

    // Ensure State content is not mutated in transition
    @Test fun coreLibraryStateMutationCheck() {
        val testNode = solver.expand(testRoot)
        assertTrue(testNode.state.stateIndex is Int,  "Is integer")
    }

    // Ensure expansion is working properly, and meta-info is transfered from state-to-state.
    @Test fun coreLibraryTestGo() {
        var nextNode = solver.expand(testRoot)
        val iterC = (2..99).random()
        for (i in 1..iterC){
            nextNode = solver.expand(nextNode)
        }
        assertTrue(nextNode.state.counter == iterC + 1,  "Test MDP Expansion")
    }
}