---
title: 'mctreesearch4j: Monte Carlo Tree Search made Easy'
tags:
  - Java Virtual Machine
  - Monte Carlo Tree Search
  - Markov Decision Process
  - Software Design
authors:
  - name: Larkin Liu
    orcid: 0000-0002-9375-1035
    affiliation: 1
  - name: Jun Tao Luo
    orcid: 
    affiliation: 2
affiliations:
 - name: University of Toronto
   index: 1
 - name: Carnegie Mellon University
   index: 2
date: 30 August 2021
bibliography: paper.bib
---

# Summary

Flexible implementations of Monte Carlo Tree Search (MCTS), combined with domain specific knowledge and hybridization with other search algorithms,  can be a very powerful for the solution of problems in complex planning. We introduce *mctreesearch4j*, a standard MCTS implementation written as a standard JVM library following key design principles of object oriented programming. We define key class abstractions allowing the MCTS library to flexibly adapt to any well defined Markov Decision Process or turn-based adversarial game. Furthermore, our library is designed to be modular and extensible, utilizing class inheritance and generic typing to standardize custom algorithm definitions. We demonstrate that the design of the MCTS implementation provides ease of adaptation for unique heuristics and customization across varying Markov Decision Process (MDP) domains. In addition, the implementation is reasonably performant and accurate for standard MDP's. In addition, via the implementation of \textit{mctreesearch4j}, the nuances of different types of MCTS algorithms are discussed.

# Statement of Need

``mctreesearch4j`` test cite [@Kocsis:2006].

Open source implementations of Monte Carlo Tree Search exist, but have not gained widespread adoption. Implementations of MCTS have been written for JVM, C/C++, and Python, yet many do not provide easy access to heuristics, nor do they implement extensible and modular state action space abstractions. Furthermore, many implementations do not provide easy access to heuristics, nor do they implement extensible and modular state action space abstractions. *mctreesearch4j* aims build on the performance advantages of a compiled language such as JVM, while simultaneously providing a complete set of capabilities, namely extensibility and modularity. Therefore, many improvements and research experiments could be performed by extending and modifying the capabilities of the base software. *mctreesearch4j* is designed to enable researchers to experiment with various MCTS strategies while standardizing the functionality of the MCTS solver and ensuring reliability, where the experiments are reproducible and the solver compatible with common JVM runtimes.

# Design Principles

``mctreesearch4j`` is designed follow three key design principles. Firstly, we design for adaptability. 

- Adaptability is defined as the ability for MDP domain to be easily integrated into the *mctreesearch4j* framework with ease via class abstractions. Our implementation seeks to simplify the adoption of MCTS solutions for a variety of domains. 
- Secondly, we design a software that is fully compatible with the Java Virtual Machine (JVM). 
- And lastly, we design to achieve a high degree of extensibility and modularity within the framework. Extensibility is the defined as the ability for key mechanisms to be reused, redefined, and enhanced, without sacrificing interoperability.

# Usage

The main abstraction that is used to define an MDP problem is the abstract class defined in the code listing MDP Interface, using generic type variables. Each of the methods correspond to specific behaviour of a discrete MDP. In \textit{mctreesearch4j} we use generic types ``StateType`` and ``ActionType`` to represent the MDP states and actions respectively. This abstract class has five members that must be implemented. These abstract class methods define the functionality of an MDP. The MDP abstraction will be used by core MCTS solvers to compute the optimal policy. The MDP interface can be written in any JVM language, we use Kotlin and Scala for this paper, with the Scala implementation from [@Liu:2021-connect4].

### MDP Interface
```kotlin
abstract class MDP<StateType, ActionType> {
    abstract fun transition(StateType, ActionType) : StateType
    abstract fun reward(StateType, ActionType?, StateType) : Double
    abstract fun initialState() : StateType
    abstract fun isTerminal(StateType) : Boolean
    abstract fun actions(StateType) : Collection<ActionType>
}
```







## Domain Abstraction

## Solver Design

# Customization

# Results

# Conclusion

# Acknowledgements

# References