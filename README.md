# Introduction

See doc/Coding Challenge.pdf for the original challenge.

# Running the code

To build and run tests:

`mvn install`

To run the sample route (included in the tests above):

* Windows: `java -cp target\libs\* -jar target\AirRoute-1.0-SNAPSHOT.jar DUB SYD`
* Linux or MacOS: `java -cp target/libs/* -jar target\AirRoute-1.0-SNAPSHOT.jar DUB SYD`

To run additional tests:

* remove @Disabled annotation on DijkstraRuntimeTest

To change the set of airport routes:

* modify src/main/resources/airroute.cfg

# Assumptions

* The goal is to assess a practical development scenario, where the requirement is to provide efficient
  correct unit-tested code, as efficiently as possible
* Java implementation
* Developed and run on JDK 16, but has few dependencies and may run well on lower versions (e.g. Java 8)

# Approach

 1. classify the problem: this is the shortest path problem, a classic Computer Science challenge
 2. choose the architecture: this is stated in the challenge (Java command-line application)
 3. identify the appropriate algorithms; there are many possible (c.f. https://en.wikipedia.org/wiki/Shortest_path_problem#Algorithms)
 4. choose an algorithm

My default algorithm for this problem would be Dijkstra or sometimes A* path-finding algorithm.
It's some time since I've solved a problem like this, so I was pleasantly surprised to find
Thorup 1999 as a new option.

Coding an algorithm is a non-trivial task (requiring more time than a coding test allows),
so I researched among the most efficient options.

# Thorup 1999

This algorithm's Java implementation is available exclusively at https://github.com/npruehs/thorup

The source code is available and has some demos but no unit tests.
In addition to the lack of tests, the key issue is running the code demonstrates that the algorithm
implementation performs worse than Dijkstra:

`Generating a random graph with 50000 vertices... done: Generated graph has 99998 edges.
Running Dijkstra with an array priority queue... took 12 ms (average of 1 passes).
Running Dijkstra with a Fibonacci heap... took 19 ms (average of 1 passes).
Running Thorup... took 31 ms for constructing the MST, 80 ms for constructing the other data structures, and 898 ms for visiting all vertices (average of 1 passes).
`
For these 2 reasons, I did not use this as a base. Given more time, I would profile the code to see if it can be easily
optimised, as the code looks correct at first glance.

# Dijkstra with Fibonacci heap

Based on the Big-O performance, this should be the next best implementation.

## Dijkstra 1

I found an implementation at https://github.com/gabormakrai/dijkstra-performance/blob/master/DijkstraPerformance/src/com/keithschwarz/FibonacciHeap.java
however the unit tests revealed a bug.

I was able to find and workaround the bug (it's in dijkstra.priority.PriorityObject#compareTo which never
returns 0 (meaning equal) as the priority is represented as a double, which the developer did not know
how to compare within a degree of precision (some cases can use Java's Double.compare(), but it's often better
to use org.apache.commons.math3.util.Precision.compareTo()).

I was able to fix this bug to make the tests pass, but it seemed high risk using an algorithm that relied on hand-built
implementations of basic collections (like PriorityQueue)). For this reason, I passed on this base.

## Dijkstra 2

Next I found https://github.com/SvenWoltmann/pathfinding which had several implementations of path-finding algorithms,
including Dijkstra with Fibonacci heap.

The code used the popular Guava collections to represent a graph (com.google.common.graph.ValueGraph),
so this looked more promising.

Running the demos within the repository showed good performance.

`Time for graph with  25,624 nodes and   102,496 edges =     21.4 ms  -->  Median after  1 iterations =     21.4 ms
`

This looked to be a promising implementation. 

# Initial build

1. I found the minimum set of classes to support the algorithm
   (DijkstraWithPriorityQueue, with NodeWrapper) and the tests (DijkstraRuntimeTest).
2. I pasted the PDF table into Excel, saved to CSV and wrote a simple parser (AirRouteConfiguration) to read the
   air route table provided into a ValueGraph<Airport, Integer>.
3. I wrote AirRouteFinder to run the algorithm and print the results.
4. I wrote a test (AirRouteTest) to try the sample route DUB to SYD, which includes 4 airports in the route. It gave the desired result.
5. I wrote a few more tests (see results below).

```
self
DUB -- DUB ( 0 )
time: 0

simple
DUB -- LHR ( 1 )
time: 1

other
DUB -- ORD ( 6 )
ORD -- LAS ( 2 )
LAS -- LAX ( 2 )
time: 10

sample
DUB -- LHR ( 1 )
LHR -- BKK ( 9 )
BKK -- SYD ( 11 )
time: 21

thereAndBackAgain
SYD -- BKK ( 11 )
BKK -- LHR ( 9 )
LHR -- DUB ( 1 )
time: 21
```

# Effort

The project took about 2.5 hours. It may have taken less if I had just implemented a naive Dijkstra myself,
but this is a result I would be happy to put into production, after testing functionally and non-functionally
on a real-world node graph.