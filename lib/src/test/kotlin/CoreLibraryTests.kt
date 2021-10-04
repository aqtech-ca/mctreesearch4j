
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import TestStochasticMDP
import ca.aqtech.mctreesearch4j.GenericSolver

class CoreLibraryTests {

    val testMDP = TestStochasticMDP()
    val iterations = 5
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

    fun coreLibraryTest(i: Int) {
        // println("cool test")
        true
    }

    @Test fun coreLibraryTestGo() {
        this.coreLibraryTest(1)
        // false
        // assertTrue(4 == 7, "failed test")
    }
}