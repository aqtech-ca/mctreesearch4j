package MCTSGamesMain


/*
fun main(arg1: String) {
    println("hahaha")
    println(arg1)
}

 */

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

    if ("GridWorldSolve" in args) {
        println("Get a Policy for Grid World via MCTS: ")
        GridWorld.main()
    }
    

    println("hahaha")
    println(args[0])

}

