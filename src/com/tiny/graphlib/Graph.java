package com.tiny.graphlib;

import java.util.*;
import java.util.stream.Collectors;
import static java.util.function.Predicate.not;

/**
 * This class represents a simple graph, where vertices and edges can be added.
 * It also allows to find a path between two vertices.
 */
public class Graph<T> implements iGraph<T> {

    private Map<iVertex<T>, List<iVertex<T>>> vertices = new HashMap<iVertex<T>, List<iVertex<T>>>();
    private String label;
    private boolean isDirected = false;

    // optimization in order to use search results before a new edge is added
    private Map<iVertex<T>, Boolean> isSearchInfoActual = new HashMap<iVertex<T>, Boolean>();

    private static StringBuilder sb = new StringBuilder();

    // This looks disputable starting with Java 9, but nevertheless
    private String concatenateStrings(String... strings) {
        sb.setLength(0);

        for(int i = 0; i < strings.length; i++){
            sb.append(strings[i]);
        }
        return sb.toString();
    }

    /**
     * Constructing an empty undirected graph
     * @param s human-readable label
     */
    public Graph(String s) {
        label = s;
    }

    /**
     * Constructing an empty graph
     * @param s human-readable label
     * @param isDir directed graph will be created if true, undirected one otherwise
     */
    public Graph(String s, boolean isDir) {
        label = s;
        isDirected = isDir;
    }

    private void setAllSearchInfoNotActual() {
        isSearchInfoActual.keySet().stream().forEach(s -> isSearchInfoActual.put(s, false));
    }

    /**
     * The method creates a new graph vertex with no dependencies
     * @throws ItemExistsException in case vertex is already inserted
     */
    @Override
    public void addVertex(T userVertex) throws ItemExistsException {
        iVertex<T> v = new Vertex<T>(userVertex);
        if (vertices.keySet().contains(v)) {
            throw new ItemExistsException(concatenateStrings("Vertex already exists: ", v.getUserVertex().toString()));
        }
        setAllSearchInfoNotActual();
        vertices.put(v, new ArrayList<iVertex<T>>());
        isSearchInfoActual.put(v, false);
    }

    private boolean isDirected() {
        return isDirected;
    }


    private iVertex<T> findVertex(T userVertex) throws ItemNotFoundException {
        iVertex<T> dummyVertex = new Vertex<T>(userVertex);

        for (iVertex<T> vertex : vertices.keySet()) {
            if (dummyVertex.equals(vertex)) {
                return vertex;
            }
        }

        throw new ItemNotFoundException(concatenateStrings("Vertex ", userVertex.toString(), " doesn't exist"));
    }

    /**
     * Adds dependencies between two existing vertices (a single dependency in case of directed graph).
     *
     * @throws ItemNotFoundException in case vertex "from" or vertex "to" doesn't exist
     * @throws ItemExistsException   in case edge is already inserted
     */
    @Override
    public void addEdge(T from, T to) throws ItemExistsException, ItemNotFoundException {

        iVertex<T> internalVertex1 = findVertex(from);
        iVertex<T> internalVertex2 = findVertex(to);

        List<iVertex<T>> v1 = vertices.get(internalVertex1);
        List<iVertex<T>> v2 = vertices.get(internalVertex2);
        if (v1 != null && v2 != null) {

            if (v1.contains(internalVertex2) || !isDirected && v2.contains(internalVertex1)) {
                throw new ItemExistsException(concatenateStrings("Already existing edge from ",
                        from.toString(), " to ", to.toString()));
            }

            setAllSearchInfoNotActual();

            v1.add(internalVertex2);
            if (!isDirected()) {
                v2.add(internalVertex1);
            }
        }
    }

    private iVertex<T> getNextVertex(Map<iVertex<T>, SearchInfoItem<T>> startPointSearchInfo) {
        return startPointSearchInfo.keySet().
                stream().filter(not(s -> startPointSearchInfo.get(s).getVisited())).
                filter(s1 -> startPointSearchInfo.get(s1).labelCalculated()).
                min(Comparator.comparing(ft -> startPointSearchInfo.get(ft).getLabel())).orElse(null);
    }


    /**
     * Returns a list of vertices which is a path from userFrom to userTo.
     * This method uses Dijkstra algorithm to find shortest path from certain vertex to others
     */
    @Override
    public List<T> getPath(T userFrom, T userTo) throws ItemNotFoundException {

        iVertex<T> internalFrom = findVertex(userFrom);
        iVertex<T> internalTo = findVertex(userTo);

        Map<iVertex<T>, SearchInfoItem<T>> startPointSearchInfo = internalFrom.getSearchInfo();

        if (!isSearchInfoActual.get(internalFrom)) {

            vertices.keySet().stream().forEach(entry -> startPointSearchInfo.put(entry, new SearchInfoItem<T>()));
            startPointSearchInfo.get(internalFrom).setLabel(0);

            iVertex<T> currentVertex = getNextVertex(startPointSearchInfo);

            while (currentVertex != null) {

                for (iVertex<T> neighbour : vertices.get(currentVertex)) {

                    int sum = startPointSearchInfo.get(currentVertex).getLabel() + 1;
                    SearchInfoItem<T> neighbourSearchInfo = startPointSearchInfo.get(neighbour);
                    if (sum < startPointSearchInfo.get(neighbour).getLabel()) {
                        neighbourSearchInfo.setLabel(sum);
                        neighbourSearchInfo.setPrevVertex(currentVertex);
                    }
                }
                startPointSearchInfo.get(currentVertex).setVisited(true);
                currentVertex = getNextVertex(startPointSearchInfo);
            }
            isSearchInfoActual.put(internalFrom, true);
        }

        return buildPath(internalFrom, internalTo, startPointSearchInfo);
    }

    // this method constructs a path from "from" to "to" using special structures calculated with Dijkstra algorithm
    // It has some heuristics added in order to find the shortest way from "start" to "end" if they are equal
    private List<T> buildPath(iVertex<T> from, iVertex<T> to, Map<iVertex<T>, SearchInfoItem<T>> startPointSearchInfo) {

        Deque<T> pathDeque = new ArrayDeque<T>();

        iVertex<T> prevVertex = null;
        if (from.equals(to)) {
            prevVertex = vertices.keySet().stream().
                    filter(s -> vertices.get(s).contains(to)).
                    min(Comparator.comparing(ft -> startPointSearchInfo.get(ft).getLabel())).orElse(null);
        }

        iVertex<T> currPrev = prevVertex == null ? to : prevVertex;

        while (currPrev != null) {
            pathDeque.addFirst(currPrev.getUserVertex());
            currPrev = startPointSearchInfo.get(currPrev).getPrevVertex();
        }

        if ((pathDeque.size() != 1 || vertices.get(from).contains(to)) && from.equals(to)) {
            pathDeque.addLast(from.getUserVertex());
        }

        return pathDeque.size() == 1 ? null : pathDeque.stream().collect(Collectors.toList());
    }

    /**
     * Returns a set of graph vertices
     */
    @Override
    public Set<T> getVertices() {

        Set<T> result = new HashSet<T>();
        vertices.keySet().stream().map(vrtx -> vrtx.getUserVertex()).forEach(result::add);
        return result;
    }


    /**
     * This graph representation follows DOT graph language format
     * (https://graphviz.gitlab.io/_pages/doc/info/lang.html),
     * and could be used for visualization f.e. using http://www.webgraphviz.com/
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph ").append(label).append(" {\n");
        for (iVertex<T> vertex : vertices.keySet()) {
            List<iVertex<T>> nbrs = vertices.get(vertex);
            if (nbrs == null || nbrs.isEmpty()) {
                sb.append(vertex).append('\n');
            } else {
                for (iVertex<T> nbr : nbrs) {
                    sb.append(vertex).append(" -> ").append(nbr).append('\n');
                }
            }
        }
        sb.append("}");
        return sb.toString();
    }
}
