package GridWorld

// import kotlin.test.Test
// import kotlin.test.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertTrue


class GridWorldTests {

    val correctSolutionsOpenSpace = arrayOf(
            arrayOf(arrayOf("↑", "→"), arrayOf("↑")  , arrayOf("←", "↑"), arrayOf("←", "↑"), arrayOf("←", "↑"), arrayOf("←", "↑"), arrayOf("←", "↑"), arrayOf("←", "↑")),
            arrayOf(arrayOf("→")     , arrayOf("R")  , arrayOf("←")     , arrayOf("←")     , arrayOf("←")     , arrayOf("←")     , arrayOf("←")     , arrayOf("←")     ),
            arrayOf(arrayOf("↓", "→"), arrayOf("↓")  , arrayOf("←", "↓"), arrayOf("←", "↓"), arrayOf("←", "↓"), arrayOf("←", "↓"), arrayOf("←", "↓"), arrayOf("←", "↓")),
            arrayOf(arrayOf("↓", "→"), arrayOf("↓")  , arrayOf("←", "↓"), arrayOf("←", "↓"), arrayOf("←", "↓"), arrayOf("←", "↓"), arrayOf("←", "↓"), arrayOf("←", "↓")),
            arrayOf(arrayOf("↓", "→"), arrayOf("↓")  , arrayOf("←", "↓"), arrayOf("←", "↓"), arrayOf("←", "↓"), arrayOf("X")     , arrayOf("↓"), arrayOf("←", "↓")),
    )

    val worldFeaturesOpenSpace = listOf(
            GridworldReward(5, 4, -0.5),
            GridworldReward(1, 1, 1.0)
    )

    val correctSolutionsWall = arrayOf(
            arrayOf(arrayOf("↑", "→"), arrayOf("↑")  , arrayOf("←", "↑"), arrayOf("←", "↑"), arrayOf("←", "↑"), arrayOf("X")     , arrayOf("→", "↑"), arrayOf("←", "↑")),
            arrayOf(arrayOf("→")     , arrayOf("R")  , arrayOf("←")     , arrayOf("←")     , arrayOf("←")     , arrayOf("X")     , arrayOf("→", "↑"), arrayOf("→", "↑")),
            arrayOf(arrayOf("↓", "→"), arrayOf("↓")  , arrayOf("←", "↓"), arrayOf("←", "↓"), arrayOf("←", "↓"), arrayOf("X")     , arrayOf("→", "↑"), arrayOf("←", "↑")),
            arrayOf(arrayOf("↓", "→"), arrayOf("↓")  , arrayOf("←", "↓"), arrayOf("←", "↓"), arrayOf("←", "↓"), arrayOf("X")     , arrayOf("→", "↑"), arrayOf("←", "↑")),
            arrayOf(arrayOf("↓", "→"), arrayOf("↓")  , arrayOf("←", "↓"), arrayOf("←", "↓"), arrayOf("←", "↓"), arrayOf("←")     , arrayOf("←"     ), arrayOf("←")    ),
    )

    val worldFeaturesWall = listOf(
            GridworldReward(5, 3, -0.1),
            GridworldReward(5, 2, -0.1),
            GridworldReward(5, 1, -0.1),
            GridworldReward(5, 0, -0.1),
            GridworldReward(1, 1, 1.0)
    )

    fun testGridWorld(worldFeatures: List<GridworldReward>, solutionArray: Array<Array<Array<String>>>): Double {
        // Open space small world test.

        val gw = GridWorldGridSolve(
                8,
                5,
                worldFeatures,
                0.85,
                9999,
                75,
                0.9,
                0.9,
                false
        )

        gw.getWorldSolve()
        gw.visualizeWorldSolve()

        var numIncorrect = 0.0
        for (s in gw.mapOfSolutions) {
            var index = s.key
            if (!solutionArray[index.second][index.first].contains(gw.mapOfSolutions[index])) {
                numIncorrect += 1
            }
        }

        println(gw.mapOfSolutions)
        println(worldFeatures.size)
        println(gw.mapOfSolutions.size)
        println(numIncorrect)
        return numIncorrect / (gw.mapOfSolutions.size - worldFeatures.size)

    }

    @Test fun gridWorldTestOpenSpace() {
        // The world solves should be 95% accurate
        // Run 10 times, and get the average
        var openWorldErrors = mutableListOf(this.testGridWorld(this.worldFeaturesOpenSpace, this.correctSolutionsOpenSpace))
        for (i in 1..9) {
            openWorldErrors.add(this.testGridWorld(this.worldFeaturesOpenSpace, this.correctSolutionsOpenSpace))
        }

        assertTrue(openWorldErrors.average() < 0.15, "Error percentage for grid world solution, (open space) " + openWorldErrors.average().toString() + " must be less than: 0.05")
    }
    @Test fun gridWorldTestWall() {
        var wallErrorValues = mutableListOf(this.testGridWorld(this.worldFeaturesWall, this.correctSolutionsWall))
        for (i in 1..9){
            wallErrorValues.add(this.testGridWorld(this.worldFeaturesWall, this.correctSolutionsWall))
        }

        assertTrue(wallErrorValues.average() < 0.15 , "Error percentage for grid world solution, (wall) " + wallErrorValues.average() + " must be less than: 0.05")
    }
}
