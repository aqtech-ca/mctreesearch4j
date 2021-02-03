import de.magoeke.kotlin.connectfour.controllers.impl.MainController
import de.magoeke.kotlin.connectfour.models.Board
import de.magoeke.kotlin.connectfour.models.GameState
import de.magoeke.kotlin.connectfour.models.Player
import java.util.*
import kotlin.random.Random

class ConnectFourGame {
    private val boardSize = 7
    private val player = Player("Player", "o")
    private val adversary = Player("Computer", "x")
    private val controller = MainController(listOf(player, adversary), boardSize)
    private val input = Scanner(System.`in`)

    fun run() {
        val moves = mutableListOf<Int>()

        println("Playing Connect 4 as \"o\" against Computer as \"x\"")

        while (controller.getGameInformation().gameState == GameState.RUNNING) {
            // display board state
            printBoard(controller.getGameInformation().board)

            if (moves.size % 2 == 0) {
                moves.add(getPlayerMove())
            }
            else {
                moves.add(getComputerMove(moves))
            }
        }

        printBoard(controller.getGameInformation().board)
        val gameEndInfo = controller.getGameInformation()
        if (gameEndInfo.gameState == GameState.RUNNING) {
            println("Game ended: draw")
        }
        else {
            println("Game ended: ${gameEndInfo.winner?.name} won")
        }
    }

    private fun getComputerMove(moves : List<Int>) : Int {
        println("Computer is thinking...")

        val game = ConnectFourMDP(ConnectFourState(moves), false)
        val stateless = StatelessSolver(
                game,
                Random.Default,
                800,
                50,
                1.4,
                0.9,
                false
        )

        stateless.buildTree()
        val move = stateless.getNextOptimalAction()

        controller.turn(move)
        return move
    }

    private fun getPlayerMove() : Int {
        while (true) {
            print("Enter move: ")

            try {
                val playerMove = input.nextInt()

                if (controller.turn(playerMove)) {
                    return playerMove
                }
            } catch(e: Exception) { }

            println("Invalid move try again.")
        }
    }

    private fun printBoard(board: Board) {
        printOutline()
        printIndicies()

        for (row in board.to2DArray()) {
            for (column in row) {
                print(if (column == null) "  " else "$column ")
            }
            println()
        }

        printIndicies()
        printOutline()
    }

    private fun printOutline() {
        for (i in 0 until boardSize) {
            print("--")
        }
        println()
    }

    private fun printIndicies() {
        for (i in 0 until boardSize) {
            print("$i ")
        }
        println()
    }
}