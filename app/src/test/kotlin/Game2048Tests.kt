package Twenty48

import ExtendedStatelessSolver
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertTrue
import kotlin.random.Random
import StatelessSolver
import kotlin.test.assertNotNull

class Game2048Tests {

    val scenarioGrid1 = arrayOf(
            arrayOf(0, 0, 2, 0),
            arrayOf(2, 2, 0, 0),
            arrayOf(64, 64, 2, 2),
            arrayOf(128, 256, 512, 1024)
    )

    val scenarioGrid2 = arrayOf(
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
                0.9,
                true
        )
        solver.constructTree(200)
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

    @Test fun game2048testSenario1() {
        this.testGame2048(this.scenarioGrid1)

    }

    @Test fun game2048testScenario2() {
        this.testGame2048(this.scenarioGrid2)
    }
}