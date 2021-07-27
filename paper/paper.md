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
    orcid: 0000-0002-2681-8922
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

Flexible implementations of Monte Carlo Tree Search (MCTS), combined with domain specific knowledge and hybridization with other search algorithms,  can be a very powerful for the solution of problems in complex planning. We introduce *mctreesearch4j*, a standard MCTS implementation written as a standard JVM library following key design principles of object oriented programming. We define key class abstractions allowing the MCTS library to flexibly adapt to any well defined Markov Decision Process or turn-based adversarial game. Furthermore, our library is designed to be modular and extensible, utilizing class inheritance and generic typing to standardize custom algorithm definitions. We demonstrate that the design of the MCTS implementation provides ease of adaptation for unique heuristics and customization across varying Markov Decision Process (MDP) domains. In addition, the implementation is reasonably performant and accurate for standard MDP's. In addition, via the implementation of *mctreesearch4j``, the nuances of different types of MCTS algorithms are discussed.

# Statement of Need

``mctreesearch4j`` test cite [@Kocsis:2006].

Open source implementations of Monte Carlo Tree Search exist, but have not gained widespread adoption. Implementations of MCTS have been written for JVM, C/C++, and Python, yet many do not provide easy access to heuristics, nor do they implement extensible and modular state action space abstractions. Furthermore, many implementations do not provide easy access to heuristics, nor do they implement extensible and modular state action space abstractions. *mctreesearch4j* aims build on the performance advantages of a compiled language such as JVM, while simultaneously providing a complete set of capabilities, namely extensibility and modularity. Therefore, many improvements and research experiments could be performed by extending and modifying the capabilities of the base software. *mctreesearch4j* is designed to enable researchers to experiment with various MCTS strategies while standardizing the functionality of the MCTS solver and ensuring reliability, where the experiments are reproducible and the solver compatible with common JVM runtimes.

# Design Principles

``mctreesearch4j`` is designed follow three key design principles. Firstly, we design for adaptability. 

- Adaptability is defined as the ability for MDP domain to be easily integrated into the *mctreesearch4j* framework with ease via class abstractions. Our implementation seeks to simplify the adoption of MCTS solutions for a variety of domains. 
- Secondly, we design a software that is fully compatible with the Java Virtual Machine (JVM). 
- And lastly, we design to achieve a high degree of extensibility and modularity within the framework. Extensibility is the defined as the ability for key mechanisms to be reused, redefined, and enhanced, without sacrificing interoperability.

# Domain Abstraction

The main abstraction that is used to define an MDP problem is the abstract class defined in the code listing MDP Interface, using generic type variables. Each of the methods correspond to specific behaviour of a discrete MDP. In *mctreesearch4j`` we use generic types ``StateType`` and ``ActionType`` to represent the MDP states and actions respectively. This abstract class has five members that must be implemented. These abstract class methods define the functionality of an MDP. The MDP abstraction will be used by core MCTS solvers to compute the optimal policy. The MDP interface can be written in any JVM language, we use Kotlin and Scala for this paper, with the Scala implementation from [@Liu:2021-connect4].

### MDP Interface
```kotlin
abstract class MDP<StateType, ActionType> {
    abstract fun transition(StateType, ActionType) : StateType
    abstract fun reward(StateType, ActionType?, StateType) : Double
    abstract fun initialState() : StateType
    abstract fun isTerminal(StateType) : Boolean
    abstract fun actions(StateType) : Collection<ActionType>
``
```

## Solver Design

![Alt text](software_design_mcts.png?raw=true "Title")

*mctreesearch4j* provides a default implementation known as ``class GenericSolver``, and an alternate ``StatefulSolver``. The abstract ``Solver`` serves as the base class for both versions, and defines a set of functionalities that all solver implementations must provide as well as a set of extensible utilities. Similar to the MDP abstraction, the solver uses a set of type parameters to provide strongly typed methods that unify and simplify the MCTS implementation. The programmer is free to select the data type or data structure that best defines how states and actions are represented in their MDP domain. Thus we can infer that, Generic and Stateful solvers have different representations of the ``NodeType``. 

The differentiation lies in their respective memory utilization of abstract node types to track states during MCTS iterations. The default ``GenericSolver`` provides a leaner implementation, where actions are tracked and no explicit states are stored permanently. The states tracked with ``GenericSolver`` are dynamic and the MDP transitions must be rerun when traversing the search tree during selection in order to infer the state. The ``StatefulSolver`` keeps an explicit record of the visited states, and additional information, such as terminality and availability of downstream actions. The extra overhead of storing the state explicitly in the MCTS node, allows the algorithm to optimize its search using information from previously visited states. This is particularly useful for deterministic games, where a re-computation of the MDP transition is not necessary to determine the state of the agent after a particular taking a specific action. This differentiation results in different implementations of the *Selection* step, while maintaining identical implementations of *Expansion*, *Simulate* and *Backpropagation*. Both implementation iterates the algorithm in the most common pattern of *Selection*, *Expansion*, *Simulation*, *Backpropagation* until a certain number of iterations.

# Customization

# Results

# Conclusion

# Acknowledgements

# References