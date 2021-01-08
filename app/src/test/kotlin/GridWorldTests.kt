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
            arrayOf(arrayOf("↓", "→"), arrayOf("↓")  , arrayOf("←", "↓"), arrayOf("←", "↓"), arrayOf("←", "↓"), arrayOf("X")     , arrayOf("←", "↓"), arrayOf("←", "↓")),
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
                1000,
                40,
                1.4,
                0.9,
                false
        )

        gw.getWorldSolve()
        gw.visualizeWorldSolve()

        var numIncorrect = 0.0
        for (s in gw.mapOfSolutions) {
            var index = s.key
            if (!solutionArray[index.second][index.first].contains(gw.mapOfSolutions[index])){
                numIncorrect += 1
            }
        }

        println(gw.mapOfSolutions)
        println(worldFeatures.size)
        println(gw.mapOfSolutions.size)
        println(numIncorrect)
        return numIncorrect/ (gw.mapOfSolutions.size - worldFeatures.size)

    }

    @Test fun gridWorldTests() {
        // The world solves should be 95% accurate
        val openSpaceGridWorldError = this.testGridWorld(this.worldFeaturesOpenSpace, this.correctSolutionsOpenSpace)
        assertTrue(openSpaceGridWorldError < 0.05 , "Error percentage for grid world solution, (open space) " + openSpaceGridWorldError.toString() + " must be less than: 0.05")

        val wallError = this.testGridWorld(this.worldFeaturesWall, this.correctSolutionsWall)
        assertTrue(wallError < 0.05 , "Error percentage for grid world solution, (wall) " + wallError.toString() + " must be less than: 0.05")
    }
}
