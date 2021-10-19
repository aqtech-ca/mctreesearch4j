package MCTSGamesMain

fun main(args: Array<String>) {

    if ("ReversiGame" in args) {
        println("Play Reversi with AI: ")
        ReversiMain.main()
    }

    if ("ReversiAdversarialSim" in args){
        println("Simulating Adversarial Reversi Competition: ")
        ReversiMain.adversarialSim()
    }

    if ("PushYourLuckSim" in args) {
        println("Run Push Your Luck MDP Simulation: ")
        PushYourLuck.main()
    }

    if ("GridWorld" in args) {
        println("Get a Policy for GridWorld via MCTS: ")
        GridWorld.main()
    }

    if ("Game2048" in args) {
        println("Get a Solution for 2048 Game via MCTS: ")
        Twenty48.main()
    }
}

