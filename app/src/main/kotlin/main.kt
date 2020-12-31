fun main() {
//    val gw = GridWorldGridSolve(
//        8,
//        5,
//        listOf(
//            GridworldReward(5, 4, -0.5),
//            // GridworldReward(3, 3, 1.0),
//            GridworldReward(1, 1, 1.0)
//        ),
//        0.85)
//
//    gw.getWorldSolve()
//    gw.visualizeWorldSolve()

    val game = ConnectFourMDP()

    // no valid move on column 1
    val no1 = ConnectFourState(listOf(1, 1, 1, 1, 1, 1, 1))

    check(game.actions(no1).count() == 6)
    check(!game.actions(no1).contains(1))
    check(!game.isTerminal(no1))

    // player 1 wins
    val winner1 = ConnectFourState(listOf(3, 0, 4, 0, 5, 0, 6))

    check(game.isTerminal(winner1))
    check(game.reward(null, null, winner1) == 1.0)

    // player 2 wins
    val winner2 = ConnectFourState(listOf(3, 0, 3, 0, 4, 0, 4, 0))

    check(game.isTerminal(winner2))
    check(game.reward(null, null, winner2) == -1.0)
}
