import Twenty48.Game2048MDP
import kotlin.random.Random
import StatefulSolver
import Twenty48.Game2048Position
import Twenty48.Game2048State

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
            arrayOf(0, 0, 0, 0),
            arrayOf(0, 0, 0, 0),
            arrayOf(64, 64, 0, 0),
            arrayOf(128, 256, 512, 1024)
    )))

    val game2048MDP = Game2048MDP(initialGameState)

    var solver = StatelessSolver(
        game2048MDP,
        Random,
        20000,
        60,
        1.4,
        0.9,
        true
    )
    solver.buildTree()
    // println("Solving at [$x, $y]")
    solver.displayTree()

}
