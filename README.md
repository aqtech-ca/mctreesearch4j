# mctreesearch4j

![Static](https://img.shields.io/static/v1?label=docs&message=latest&color=blue&style=flat-square)
![kotlin-version](https://kotlin-version.aws.icerock.dev/kotlin-version?group=ca.aqtech&name=mctreesearch4j)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/ca.aqtech/mctreesearch4j/badge.svg?style=plastic)](https://maven-badges.herokuapp.com/maven-central/ca.aqtech/mctreesearch4j)

An robust Implementation of the Monte Carlo Tree Search (MCTS) algorithm for the JVM.

## Objective

Flexible implementations of Monte Carlo Tree Search (MCTS), combined with domain specific knowledge and hybridization with other search algorithms, can be a very powerful for the solution of problems in complex planning. We introduce *mctreesearch4j*, a standard MCTS implementation written as a standard JVM library following key design principles of object oriented programming. We define key class abstractions allowing the MCTS library to flexibly adapt to any well defined Markov Decision Process or turn-based adversarial game. Furthermore, our library is designed to be modular and extensible, utilizing class inheritance and generic typing to standardize custom algorithm definitions. We demonstrate that the design of the MCTS implementation provides ease of adaptation for unique heuristics and customization across varying Markov Decision Process (MDP) domains. In addition, the implementation is reasonably performant and accurate for standard MDP’s. In addition, via the implementation of *mctreesearch4j*, the nuances of different types of MCTS algorithms are discussed.

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
            ├── Solver.kt
            ├── StateNode.kt
            ├── StatefulSolver.kt
            ├── ActionNode.kt
            └── StatelessSolver.kt

```

The MCTS lib can be implemented by creating an interface implementing a Markov Decision Process (MDP) class, to communicate with the MCTS solver. The MCTS solver comes in two flavours `StatefulSolver.kt` and `StatelessSolver.kt`. The `StatefulSolver.kt` records both the action history and state, whereas the `StatelessSolver.kt` only records actions taken, and infers the state from actions taken.

## Defining an MDP

`MDP.kt` defines the interface of the MDP. To run the MCTS solver, you must extend this `MDP.kt` class in any JVM language, and provide your specific definitions. 

```kotlin
abstract class MDP<StateType, ActionType> {
    abstract fun transition(state: StateType, action: ActionType) : StateType
    /* Class method that returns a legal state given current state (StateType) and action taken (ActionType) */

    abstract fun reward(previousState: StateType?, action: ActionType?, state: StateType) : Double
    /* Class method to return a reward (Double) given state transitions parameters */

    abstract fun initialState() : StateType
    /* Return the initial state of the MCTS (StateType) */

    abstract fun isTerminal(state: StateType) : Boolean
    /* Return boolean indicating if the state is terminal. */

    abstract fun actions(state: StateType) : Iterable<ActionType>
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
    implementation "ca.aqtech.mctreesearch4j:0.0.3"
}
```

### Scala Integration (SBT)

```sbt
libraryDependencies ++= Seq(
  "ca.aqtech" % "mctreesearch4j" % "0.0.3"
)
```

### Cite mctreesearch4j

Currently, to cite our framework, please cite our preprint:

```
@article{mctreesearch4j,
  author = {Liu, Larkin and Luo, Jun Tao},
  title = {An Extensible and Modular Design and Implementation of Monte Carlo Tree Search for the JVM},
  journal = {Preprint},
  year = {2021},
  url = {https://www.preprints.org/manuscript/202107.0622/v1},
  doi = {doi:10.20944/preprints202107.0622.v1}
 } 
```
