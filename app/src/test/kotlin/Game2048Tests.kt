package Twenty48

import ExtendedSolver.ExtendedStatelessSolver
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertTrue
import kotlin.random.Random
import ca.aqtech.mctreesearch4j.GenericSolver
import kotlin.test.assertNotNull

class Game2048Tests {

    val scenarioGrid = arrayOf(
            arrayOf(1024, 0, 0, 0),
            arrayOf(512, 512, 0, 0),
            arrayOf(0, 0, 0, 0),
            arrayOf(0, 0, 0, 0)
    )

    fun testGame2048(inputGrid: Array<Array<Int>>) {
        var testGrid = inputGrid.copyOf()

        val initialGameState = Game2048State(Game2048Position(testGrid))
        val game2048MDP = Game2048MDP(initialGameState)

        var solver = ExtendedStatelessSolver(
                game2048MDP,
                500,
                1.4,
                0.95,
                true
        )
        solver.runTreeSearch(200)
        solver.displayTree()

        println("optimalAction")
        println(solver.extractOptimalAction().toString())

        println("optimal Horizon")
        val solList = solver.getOptimalHorizon().map { it.toString() }

        println(solList)
        // simply replay the 2048 game using the solution

        var gc = Game2048Controller()
        for (a in solList){
            testGrid = gc.manipulateGrid(testGrid, a)
        }
        println(Game2048State(Game2048Position(testGrid)).toString())

        val score = Game2048State(Game2048Position(testGrid)).score
        println(score)

        assertNotNull(score, "2048 score is not null.")

        if (score < 2048)
        {
            assertTrue(score >= 2048, "Scenario reached a suboptimal solution of score: $score")
        }
    }

    @Test fun game2048testScenario1() {
        this.testGame2048(this.scenarioGrid)
    }
}