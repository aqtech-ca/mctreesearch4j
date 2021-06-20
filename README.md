# mctreesearch4j

![Static](https://img.shields.io/static/v1?label=docs&message=latest&color=blue&style=flat-square)
![kotlin-version](https://kotlin-version.aws.icerock.dev/kotlin-version?group=ca.aqtech&name=mctreesearch4j)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/ca.aqtech/mctreesearch4j/badge.svg?style=plastic)](https://maven-badges.herokuapp.com/maven-central/ca.aqtech/mctreesearch4j)

An robust Implementation of the Monte Carlo Tree Search (MCTS) algorithm for the JVM.

## Objective

We create a robust and modular implementation of the MCTS algorithm for the JVM written in Kotlin. We demonstrate that the design of the MCTS implementation enables it to flexibly adapt to any single player or adversarial game, with unique heuristics and customization which can be implemented with ease. In addition, the implementation is reasonably efficient in both time and space complexity and accurate for a standard MDP, when compared to state-of-the-art dynamic programming counterparts.

## Structure

The library contains the implementation of the MCTS algorithm for single player and adversarial games. The project is compiled using `gradle`.

```
lib
└── 
    ...
    └── src.main.kotlin
        └── ca.aqtech.mctreesearch4j
            ├── MDP.kt
            ├── Node.kt
            ├── SolverBase.kt
            ├── StateNode.kt
            ├── StatefulSolver.kt
            ├── StatelessActionNode.kt
            └── StatelessSolver.kt

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

## Maven Central

This codebase is available directly via the [Maven Central repository - mctreesearch4j](https://search.maven.org/artifact/ca.aqtech/mctreesearch4j). In principle, it 
has full compatibility with any JVM language. 

### Kotlin Integration (groovy)
```groovy
dependencies {
    implementation "ca.aqtech.mctreesearch4j:0.0.2"
}
```

### Scala Integration (SBT)

```sbt
libraryDependencies ++= Seq(
  "ca.aqtech" % "mctreesearch4j" % "0.0.2"
)
```