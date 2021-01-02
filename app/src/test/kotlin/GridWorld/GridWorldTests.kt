package GridWorld



import kotlin.test.Test
import kotlin.test.assertTrue

class GridWorldTests {

    fun testOpenSpaceGriwWorld(): Double {
        // Open space small world test.
        val worldFeatures = listOf(
                GridworldReward(5, 4, -0.5),
                GridworldReward(1, 1, 1.0)
        )

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

        // Correct solutions

        val correctSolutions = arrayOf(
                arrayOf(arrayOf("↑", "→"), arrayOf("↑")  , arrayOf("←", "↑"), arrayOf("←", "↑"), arrayOf("←", "↑"), arrayOf("←", "↑"), arrayOf("←", "↑"), arrayOf("←", "↑")),
                arrayOf(arrayOf("→")     , arrayOf("R")  , arrayOf("←")     , arrayOf("←")     , arrayOf("←")     , arrayOf("←")     , arrayOf("←")     , arrayOf("←")     ),
                arrayOf(arrayOf("↓", "→"), arrayOf("↓")  , arrayOf("←", "↓"), arrayOf("←", "↓"), arrayOf("←", "↓"), arrayOf("←", "↓"), arrayOf("←", "↓"), arrayOf("←", "↓")),
                arrayOf(arrayOf("↓", "→"), arrayOf("↓")  , arrayOf("←", "↓"), arrayOf("←", "↓"), arrayOf("←", "↓"), arrayOf("←", "↓"), arrayOf("←", "↓"), arrayOf("←", "↓")),
                arrayOf(arrayOf("↓", "→"), arrayOf("↓")  , arrayOf("←", "↓"), arrayOf("←", "↓"), arrayOf("←", "↓"), arrayOf("X")     , arrayOf("←", "↓"), arrayOf("←", "↓")),
        )

        gw.getWorldSolve()
        gw.visualizeWorldSolve()

        var numIncorrect = 0.0
        for (s in gw.mapOfSolutions) {
            var index = s.key
            if (!correctSolutions[index.second][index.first].contains(gw.mapOfSolutions[index])){
                numIncorrect += 1
            }
        }

        // The open world solve should be 90% accurate

        // println(gw.mapOfSolutions)
        // println(worldFeatures.size)
        // println(gw.mapOfSolutions.size)
        // println(numIncorrect)
        return numIncorrect/ (gw.mapOfSolutions.size - worldFeatures.size)

    }

    @Test fun gridWorldTests() {
        val openSpaceGridWorldError = this.testOpenSpaceGriwWorld()
        assertTrue(openSpaceGridWorldError < 0.05 , "Error percentage for grid world solution, " + openSpaceGridWorldError.toString() + " must be less than: 0.05")
    }

}
