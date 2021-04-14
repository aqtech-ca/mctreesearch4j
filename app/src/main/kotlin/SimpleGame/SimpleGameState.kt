package SimpleGame
import java.util.Random

class SimpleGameState(var mu1: Double = 0.0, var mu2: Double = 0.0) {

    val rand = Random()

    fun getReward(action: SimpleGameAction): Double {
        return when {
            action === SimpleGameAction.ACTION1 -> {rand.nextGaussian() + mu1}
            action === SimpleGameAction.ACTION2 -> {rand.nextGaussian() + mu2}
            else -> 0.0
        }
    }
}