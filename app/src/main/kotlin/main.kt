import kotlin.random.Random

fun main() {

    val game = ConnectFourGame()
    game.run()

    return

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

//    val game = ConnectFourMDP()
//
//    // no valid move on column 1
//    val no1 = ConnectFourState(listOf(1, 1, 1, 1, 1, 1, 1))
//
//    check(game.actions(no1).count() == 6)
//    check(!game.actions(no1).contains(1))
//    check(!game.isTerminal(no1))
//
//    // player 1 wins
//    val winner1 = ConnectFourState(listOf(3, 0, 4, 0, 5, 0, 6))
//
//    check(game.isTerminal(winner1))
//    check(game.reward(null, null, winner1) == 1.0)
//
//    // player 2 wins
//    val winner2 = ConnectFourState(listOf(3, 0, 3, 0, 4, 0, 4, 0))
//
//    check(game.isTerminal(winner2))
//    check(game.reward(null, null, winner2) == -1.0)

    // 0 1 2 3 4 5 6
    // =============
    // 0 0 0 0 1 0 2
    // 0 0 0 0 2 0 1
    // 0 0 1 1 2 0 2
    // 0 0 2 2 2 0 1
    // 0 0 1 2 1 0 1
    // 0 0 1 2 2 2 1
    // 0 0 2 1 1 1 2
    val badMove5 = ConnectFourState(listOf(
            3, 3, 4, 2,
            5, 6, 2, 3,
            2, 3, 3, 2,
            2, 4, 4, 5,
            6, 4, 6, 4,
            6, 6, 6, 4,
            4, 6 ))

    // 0 1 2 3 4 5 6
    // =============
    // 0 0 0 0 0 0 0
    // 0 0 0 0 0 0 0
    // 0 0 0 0 0 0 0
    // 0 0 0 0 0 0 0
    // 0 0 0 0 0 0 0
    // 0 0 0 0 0 0 0
    // 2 2 2 1 1 1 0

    val goodMove6 = ConnectFourState(listOf(
            3, 2, 4, 1,
            5, 0 ))

    // 0 1 2 3 4 5 6
    // =============
    // 0 0 0 0 0 0 0
    // 1 2 0 1 1 1 2
    // 1 2 0 2 2 2 1
    // 2 1 0 1 1 1 2
    // 2 1 0 2 2 2 1
    // 1 2 0 1 1 1 2
    // 2 1 0 2 2 2 1

    val ambiguous = ConnectFourState(listOf(
            6, 6, 6, 6,
            6, 6, 1, 5,
            5, 5, 5, 5,
            5, 4, 4, 4,
            4, 4, 4, 3,
            3, 3, 3, 3,
            3, 0, 0, 1,
            1, 0, 1, 0,
            0, 1, 0, 1))

    val testGame = ConnectFourMDP(ambiguous)

    println("Stateless Solver:")

    val stateless = StatelessSolver(
            testGame,
            Random.Default,
            800,
            50,
            1.4,
            0.9,
            false
    )

    stateless.buildTree()
    stateless.displayTree()

    println("Optimal action: ${stateless.getNextOptimalAction()}")

    println("Adversarial Solver:")

    val adversarial = AdversarialStatelessSolver(
            testGame,
            Random.Default,
            800,
            50,
            1.4,
            0.9,
            false
    )

    adversarial.buildTree()
    adversarial.displayTree()

    println("Optimal action: ${adversarial.getNextOptimalAction()}")
}
