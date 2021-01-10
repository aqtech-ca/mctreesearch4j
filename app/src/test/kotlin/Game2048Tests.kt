package Twenty48

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertTrue
import kotlin.random.Random
import StatelessSolver

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

    fun testGame2048(inputGrid: Array<Array<Int>>  ): Int? {

        var testGrid = inputGrid.copyOf()

        val initialGameState = Game2048State(Game2048Position(testGrid))
        val game2048MDP = Game2048MDP(initialGameState)

        var solver = StatelessSolver(
                game2048MDP,
                Random,
                500,
                10,
                1.4,
                0.9,
                true
        )
        solver.buildTree()
        solver.displayTree()

        println("optimalAction")
        println(solver.getNextOptimalAction().toString())

        println("optimal Horizon")
        val solList = solver.getOptimalHorizon().map { it.toString() }

        println(solList)
        // simply replay the 2048 game using the solution

        var gc = Game2048Controller()
        for (a in solList){
            testGrid = gc.manipulateGrid(testGrid, a)
        }
        println(Game2048State(Game2048Position(testGrid)).toString())
        println(Game2048State(Game2048Position(testGrid)).score.toString())

        return Game2048State(Game2048Position(testGrid)).score

    }

    @Test fun game2048ests() {
        // The world solves should be 95% accurate
        val scenario1Score = this.testGame2048(this.scenarioGrid1)
        val scenario2Score = this.testGame2048(this.scenarioGrid2)
        assertTrue(scenario1Score != null, "2048 score is not null.")
        if (scenario1Score != null) {
            assertTrue(scenario1Score >= 2048 , "Scenario1 reached a suboptimal solution of socre: " + scenario1Score.toString() )
        }

        assertTrue(scenario2Score != null, "2048 score is not null.")
        if (scenario2Score != null) {
            assertTrue(scenario2Score >= 2048 , "Scenario2 reached a suboptimal solution of socre: " + scenario1Score.toString() )
        }
    }
}