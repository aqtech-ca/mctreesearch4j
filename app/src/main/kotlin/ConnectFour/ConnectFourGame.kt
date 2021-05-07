package ConnectFour

import mcts.StatelessSolver
import de.magoeke.kotlin.connectfour.controllers.impl.MainController
import de.magoeke.kotlin.connectfour.models.Board
import de.magoeke.kotlin.connectfour.models.GameState
import de.magoeke.kotlin.connectfour.models.Player
import java.util.*
import kotlin.random.Random

class ConnectFourGame {
    private val boardSize = 7
    private val input = Scanner(System.`in`)

    fun run() {
        val player = Player("Player", "o")
        val computer = Player("Computer", "x")
        val moves = mutableListOf<Int>()

        println("Playing Connect 4 as \"o\" against Computer as \"x\"")
        var playerOrder = 0
        do {
            print("Play as player 1 or 2? ")
            playerOrder = input.nextInt()
        } while (playerOrder != 1 && playerOrder != 2)

        val controller = MainController(if (playerOrder == 1) listOf(player, computer) else listOf(computer, player), boardSize)
        var gameInfo = controller.getGameInformation()

        while (gameInfo.gameState == GameState.RUNNING) {
            // display board state
            printBoard(controller.getGameInformation().board)

            if (gameInfo.currentPlayer.name == player.name) {
                moves.add(getPlayerMove(controller))
            }
            else {
                moves.add(getComputerMove(controller, moves))
            }
            gameInfo = controller.getGameInformation()
        }

        printBoard(controller.getGameInformation().board)
        if (gameInfo.gameState == GameState.RUNNING) {
            println("Game ended: draw")
        }
        else {
            println("Game ended: ${gameInfo.winner?.name} won")
        }
    }

    private fun getComputerMove(controller: MainController, moves : List<Int>) : Int {
        println("Computer is thinking...")

        val game = ConnectFourMDP(ConnectFourState(moves), moves.size % 2 == 0)
        val stateless = StatelessSolver(
                game,
                800,
                1.4,
                0.9,
                false
        )

        stateless.constructTree(50)
        val move = stateless.extractOptimalAction()

        controller.turn(move)
        return move
    }

    private fun getPlayerMove(controller: MainController) : Int {
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