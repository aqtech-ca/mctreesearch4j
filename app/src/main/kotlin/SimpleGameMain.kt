import SimpleGame.SimpleGameMDP
import java.io.File


fun main() {

    val sgMDP = SimpleGameMDP()

    var solver = ExtendedStatelessSolver(
        sgMDP,
            5,
            0.07,
            0.6,
            false
    )

    solver.constructTree(9999)
    solver.displayTree()
    val optimalHorizon = solver.getOptimalHorizon()
    println(optimalHorizon.toString())
    println(optimalHorizon.size)

    // Write data
    val path = System.getProperty("user.dir")
    println("Working Directory = $path")

    val fileName = "outputs/sg_output.txt"
    val outputFile = File(fileName)

    outputFile.printWriter().use { out ->
        out.println(solver.explorationTermHistory.joinToString(", "))
    }

}