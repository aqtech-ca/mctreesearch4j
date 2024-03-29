import ca.aqtech.mctreesearch4j.MDP

class TestStochasticMDP(val bias: Double = 0.75): MDP<TestStochasticState, TestStochasticAction>()  {
    val maxCounter = 10
    private val _allActions = TestStochasticAction.values().toList()

    override fun initialState(): TestStochasticState {
        return TestStochasticState(0)
    }

    override fun isTerminal(state: TestStochasticState): Boolean {
        return false
    }

    override fun reward(previousState: TestStochasticState?, action: TestStochasticAction?, state: TestStochasticState): Double {
        return state.stateIndex.toDouble() * 2
    }

    override fun transition(state: TestStochasticState, action: TestStochasticAction): TestStochasticState {
        var directionIndex = 0
        if (Math.random() < bias) {
            if (action == TestStochasticAction.valueOf("LEFT")) directionIndex = state.stateIndex - 1
            if (action == TestStochasticAction.valueOf("RIGHT")) directionIndex = state.stateIndex + 1
        } else {
            if (action == TestStochasticAction.valueOf("LEFT")) directionIndex = state.stateIndex + 1
            if (action == TestStochasticAction.valueOf("RIGHT")) directionIndex = state.stateIndex - 1
        }

        return TestStochasticState(directionIndex, state.counter + 1)
    }

    override fun actions(state: TestStochasticState): Collection<TestStochasticAction> {
        return _allActions
    }

}

