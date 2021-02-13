# mctreesearch4j

An robust Implementation of the Monte Carlo Tree Search Algorithm for the JVM

## Objective

We create an robust and modular implemntation of the MCTS algorithm for the JVM written in Kotlin. We demonstrate that the design of the MCTS implementation enables it to flexibly adapt to any single player or adversarial game, with unique heuristics and customization which can be implemented with ease. In addition, the implementation is reasonably efficient in both time and space complexity and accurate for a standard MDP, when compared to state-of-the-art dynamic programming counterparts.

## Structure

The library contains the implmentation of the MCTS algorithm for single player and adversarial games. The project is compiled using `gradle`.

```
lib
├── {gradle_files ...}
└── src
    └── main
        └── kotlin
            ├── ActionNode.kt
            ├── AdversarialStatelessSolver.kt
            ├── IDistribution.kt
            ├── MDP.kt
            ├── Node.kt
            ├── NodeBase.kt
            ├── POMDP.kt
            ├── ProbabilisticElement.kt
            ├── SimulationState.kt
            ├── SparseCategoricalDistribution.kt
            ├── StateActionNode.kt
            ├── StateNode.kt
            ├── StatefulSolver.kt
            ├── StatelessSolver.kt
            └── UniformDistribution.kt

```

The MCTS lib can be implemented by creating an interface implementing a Markov Decision Process (MDP) class, to communicate with the MCTS solver. The MCTS solver comes in two flavours `StatefulSolver.kt` and `StatelessSolver.kt`. The `StatefulSolver.kt` records both the action history and state, whereas the `StatelessSolver.kt` only records actions taken, and infers the state from actions taken.

## Defining an MDP

`MDP.kt` defines the interface of the MDP. To run the MCTS solver, you must extend this `MDP.kt` class in any JVM language, and provide your specific definitions. 

```kotlin
abstract class MDP<TState, TAction> {
    abstract fun transition(state: TState, action: TAction) : TState
    /* Class method that returns a legal state given current state (TState) and action taken (TAction) */

    abstract fun reward(previousState: TState?, action: TAction?, state: TState) : Double
    /* Class method to return a reward (Double) given state transitions parameters */

    abstract fun initialState() : TState
    /* Return the initial state of the MCTS (TState) */

    abstract fun isTerminal(state: TState) : Boolean
    /* Return boolean indicating if the state is terminal. */

    abstract fun actions(state: TState) : Iterable<TAction>
    /* Return an Iterable of legal actions given a current state. */
}
```

## Examples

In the `app/` folder of this repository, you may see various examples of game engines interfacing with the MDP controller.

### GridWorld

See `app/src/main/kotlin/GridWorld/` for the MDP implementation.





