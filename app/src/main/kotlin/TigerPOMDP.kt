class TigerPOMDP : POMDP<TigerState, TigerAction, TigerObservation>() {

    override fun InitialState(): IDistribution<TigerState> {
        return UniformDistribution(TigerState.values().toList())
    }

    override fun Observation(previousState: TigerState, action: TigerAction, state: TigerState): IDistribution<TigerObservation> {
        if (action == TigerAction.LISTEN) {
            if (previousState == TigerState.LEFT) {
                return SparseCategoricalDistribution(listOf(
                        ProbabilisticElement(TigerObservation.LEFT, 0.85),
                        ProbabilisticElement(TigerObservation.RIGHT, 0.15)))
            }
            else {
                return SparseCategoricalDistribution(listOf(
                        ProbabilisticElement(TigerObservation.LEFT, 0.15),
                        ProbabilisticElement(TigerObservation.RIGHT, 0.85)))
            }
        }
        else {
            return UniformDistribution(TigerObservation.values().toList()) // reset
        }
    }

    override fun Reward(previousState: TigerState, action: TigerAction, state: TigerState, observation: TigerObservation): Double {
        if (action == TigerAction.LISTEN) {
            return -1.0
        }
        else if ((action == TigerAction.LEFT && state == TigerState.LEFT)
                || (action == TigerAction.RIGHT && state == TigerState.RIGHT) ) {
            return -100.0 // found tiger
        }
        else {
            return 10.0 // escaped
        }
    }

    override fun Transition(state: TigerState, action: TigerAction): IDistribution<TigerState> {
        if (action == TigerAction.LISTEN) {
            return UniformDistribution(listOf(state)) // status quo
        }
        else {
            return UniformDistribution(TigerState.values().toList()) // reset
        }
    }

}