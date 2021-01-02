
import GridWorld.GridWorldGridSolve
import GridWorld.GridworldReward


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

fun main() {

    println(testOpenSpaceGriwWorld())


    // Wall test small world test.

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

    val gwWall = GridWorldGridSolve(
        8,
        5,
        worldFeaturesWall,
        0.85
    )

    gwWall.getWorldSolve()
    gwWall.visualizeWorldSolve()

    var numIncorrectWall = 0.0
    for (s in gwWall.mapOfSolutions) {
        var index = s.key
        if (!correctSolutionsWall[index.second][index.first].contains(gwWall.mapOfSolutions[index])){
            numIncorrectWall += 1
        }
    }
    println(numIncorrectWall)
    println(numIncorrectWall / (gwWall.mapOfSolutions.size - worldFeaturesWall.size) )


}




