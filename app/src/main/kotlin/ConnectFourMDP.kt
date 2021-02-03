import de.magoeke.kotlin.connectfour.controllers.impl.MainController
import de.magoeke.kotlin.connectfour.models.GameState
import de.magoeke.kotlin.connectfour.models.Player

class ConnectFourMDP(
    private val startingState: ConnectFourState = ConnectFourState(emptyList()),
    private val optimizeForPlayerOne: Boolean = true
)  : MDP<ConnectFourState, Int>() {
    private val player = Player("Player1", "1")
    private val adversary = Player("Player2", "2")
    private val boardSize = 7

    override fun actions(state: ConnectFourState): Iterable<Int> {
        val validActions = mutableListOf<Int>()

        for (playerAction in 0..boardSize) {
            // Reset game state
            val controller = MainController(listOf(player, adversary), 7)

            // Run the game to the current state
            for (step in state.steps) {
                controller.turn(step)
            }

            // Check validity of player action
            if (controller.turn(playerAction)) {
                validActions.add(playerAction)
            }
        }

        return validActions
    }

    override fun initialState(): IDistribution<ConnectFourState> {
        return UniformDistribution(listOf(startingState))
    }

    override fun isTerminal(state: ConnectFourState): Boolean {
        val controller = MainController(listOf(player, adversary), 7)

        // Run the game to the current state
        for (step in state.steps) {
            controller.turn(step)
        }

        // Check if game has ended
        return controller.getGameInformation().gameState != GameState.RUNNING
    }

    override fun reward(previousState: ConnectFourState?, action: Int?, state: ConnectFourState): Double {
        val controller = MainController(listOf(player, adversary), 7)

        // Run the game to the current state
        for (step in state.steps) {
            controller.turn(step)
        }

        // Get game result
        val gameInfo = controller.getGameInformation()

        val playerCorrectionFactor = if (optimizeForPlayerOne) 1.0 else -1.0

        return when (gameInfo.gameState) {
            GameState.DRAW -> 0.0
            GameState.WON -> if (gameInfo.winner?.name == player.name) playerCorrectionFactor else -playerCorrectionFactor
            GameState.RUNNING -> 0.0
        }
    }

    override fun transition(state: ConnectFourState, action: Int): IDistribution<ConnectFourState> {
        // New state consists of adding the new step to the existing state
        val newSteps = state.steps.toMutableList()
        newSteps.add(action)

        return UniformDistribution(listOf(ConnectFourState(newSteps)))
    }
}