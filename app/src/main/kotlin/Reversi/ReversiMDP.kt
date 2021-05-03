package Reversi

import mcts.MDP
import java.awt.Point

class ReversiMDP(private val initialState: ReversiState) : MDP<ReversiState, Point>(){
    override fun actions(state: ReversiState): Collection<Point> {
        return ReversiController.resolveFeasibleMoves(state)
    }

    override fun initialState(): ReversiState {
        return initialState.clone()
    }

    override fun isTerminal(state: ReversiState): Boolean {
        return state.currentPlayer == ReversiSquare.EMPTY
    }

    override fun reward(previousState: ReversiState?, action: Point?, state: ReversiState): Double {
        val opponentSquare = ReversiController.getOpponent(initialState.currentPlayer)
        var score = 0.0
        for (square in state.squares.flatten()) {
            if (square == opponentSquare) {
                score--
            }
            else if (square == initialState.currentPlayer) {
                score++
            }
        }

        return score / (initialState.size*initialState.size)
    }

    override fun transition(state: ReversiState, action: Point): ReversiState {
        val newState = state.clone()
        ReversiController.executeMove(newState, action)
        return newState
    }
}