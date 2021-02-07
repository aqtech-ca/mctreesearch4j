package PushYourLuck

enum class PushYourLuckAction {
    ROLL, CASHOUT;

    override fun toString(): String {
        return when (this) {
            ROLL -> "roll"
            CASHOUT -> "cashout"
        }
    }
}