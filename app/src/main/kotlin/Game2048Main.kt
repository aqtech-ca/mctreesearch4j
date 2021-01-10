import Twenty48.Game2048MDP
import kotlin.random.Random
import StatefulSolver
import Twenty48.Game2048Position
import Twenty48.Game2048State
import Twenty48.Game2048Controller

fun main() {

    /*
    val initialGameState = Game2048State(Game2048Position(arrayOf(
            arrayOf(1024, 0, 0, 0),
            arrayOf(512, 512, 0, 0),
            arrayOf(0, 0, 0, 0),
            arrayOf(0, 0, 0, 0)
    )))
    */
    val initialGameState = Game2048State(Game2048Position(arrayOf(
            arrayOf(0, 0, 2, 0),
            arrayOf(2, 2, 0, 0),
            arrayOf(64, 64, 2, 2),
            arrayOf(128, 256, 512, 1024)
    )))

    var testGrid = arrayOf(
            arrayOf(0, 0, 2, 0),
            arrayOf(2, 2, 0, 0),
            arrayOf(64, 64, 2, 2),
            arrayOf(128, 256, 512, 1024)
    )

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
    println(solList )

    // simply replay the 2048 game using the solution
    var gc = Game2048Controller()

    for (a in solList){
        testGrid = gc.manipulateGrid(testGrid, a)
    }
    println(Game2048State(Game2048Position(testGrid)).toString())
    println(Game2048State(Game2048Position(testGrid)).score.toString())

}
