# Description and usage
The project contains a simple graph library. It has following functionality:
* adding vertices
* adding edges
* finding a path between two vertices. According to implementation resulted path is the shortest one.

## Getting path between two vertices
Path calculation is based on Dijkstra algorithm (with some improvements allowing to find path from some vertex to itself).
Data structure described below is built and linked to the search start vertex.
Search results are considered as actual to avoid redundant calculations in case graph isn't changed.
![Internal structures](Internals.png)

# Javadoc

To generate java documention execute:

`mvn javadoc:javadoc`

# Usage

To run the tests and view the coverage execute:

`mvn verify`

View tests execution status:

`target/surefire-reports/index.html`

View coverage results:

`target/site/jacoco/index.html`

# Components and tools used:

## Test framework

TestNG - https://testng.org

maven-surefire-plugin - to execute the unit tests

## Code coverage

jacoco-maven-plugin - https://www.eclemma.org/jacoco/trunk/doc/maven.html