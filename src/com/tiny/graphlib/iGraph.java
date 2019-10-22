package com.tiny.graphlib;

import java.util.List;
import java.util.Set;

/**
 * A simple graph interface.
 * It allows to:
 * 1) add vertices
 * 2) add edges
 * 3) find a path between two vertices
 */

public interface iGraph<T> {
    /**
     * Сreates a new graph vertex with no dependencies
     *
     * @throws ItemExistsException in case vertex is already inserted
     */
    void addVertex(T v) throws ItemExistsException;

    /**
     * Adds dependencies between two existing vertices (a single dependency in case of directed graph)
     *
     * @throws ItemNotFoundException in case vertex "from" or vertex "to" doesn't exist
     * @throws ItemExistsException   in case edge is already inserted
     */
    void addEdge(T v, T neighbour) throws ItemExistsException, ItemNotFoundException;

    /**
     * Returns a list of vertices which is a path from "from" to "to"
     */
    List<T> getPath(T from, T to) throws ItemNotFoundException;

    /**
     * Returns a set of graph vertices
     */
    Set<T> getVertices();

    /**
     * This method provides human-readable representation of the graph
     */
    String toString();

}
