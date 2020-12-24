
import GridWorld.GridWorldGridSolve
import GridWorld.GridworldReward

fun main() {

    // Open space small world test.
    val worldFeatures = listOf(
        GridworldReward(5, 4, -0.5),
        GridworldReward(1, 1, 1.0)
    )

    val gw = GridWorldGridSolve(
        8,
        5,
        worldFeatures,
        0.85
    )

    // Correct solutions

    val correctSolutions = arrayOf(
        arrayOf(arrayOf("↑", "→"), arrayOf("↑")  , arrayOf("←", "↑"), arrayOf("←", "↑"), arrayOf("←", "↑"), arrayOf("←", "↑"), arrayOf("←", "↑"), arrayOf("←", "↑")),
        arrayOf(arrayOf("→")     , arrayOf("R")  , arrayOf("←")     , arrayOf("←")     , arrayOf("←")     , arrayOf("←")     , arrayOf("←")     , arrayOf("←")     ),
        arrayOf(arrayOf("↓", "→"), arrayOf("↓")  , arrayOf("←", "↓"), arrayOf("←", "↓"), arrayOf("←", "↓"), arrayOf("←", "↓"), arrayOf("←", "↓"), arrayOf("←", "↓")),
        arrayOf(arrayOf("↓", "→"), arrayOf("↓")  , arrayOf("←", "↓"), arrayOf("←", "↓"), arrayOf("←", "↓"), arrayOf("←", "↓"), arrayOf("←", "↓"), arrayOf("←", "↓")),
        arrayOf(arrayOf("↓", "→"), arrayOf("↓")  , arrayOf("←", "↓"), arrayOf("←", "↓"), arrayOf("←", "↓"), arrayOf("←", "↓"), arrayOf("←", "↓"), arrayOf("←", "↓")),
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

    println(gw.mapOfSolutions)
    println(worldFeatures.size)
    println(gw.mapOfSolutions.size)

    println(numIncorrect)
    println(numIncorrect/ (gw.mapOfSolutions.size - worldFeatures.size) )

}




