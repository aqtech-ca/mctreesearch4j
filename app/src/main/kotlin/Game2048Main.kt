import Twenty48.Game2048MDP
import kotlin.random.Random
import StatefulSolver

fun main() {

    val game2048MDP = Game2048MDP()

    var solver = StatefulSolver(
        game2048MDP,
        Random,
        1000,
        999,
        1.4,
        0.9,
        true
    )
    solver.buildTree()
    // println("Solving at [$x, $y]")
    solver.displayTree()

}
