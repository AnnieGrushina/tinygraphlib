package com.tiny.graphlib;

import java.util.HashMap;
import java.util.Map;

public class Vertex<T> implements iVertex<T> {

    private T userVertex;
    private Map<iVertex<T>, SearchInfoItem<T>> searchInfoMap = new HashMap<iVertex<T>, SearchInfoItem<T>>();

    public Vertex(T userV) {
        userVertex = userV;
    }

    public Map<iVertex<T>, SearchInfoItem<T>> getSearchInfo() {
        return searchInfoMap;
    }

    @Override
    public T getUserVertex() {
        return userVertex;
    }

    @Override
    public String toString() {
        return userVertex.toString();
    }


    @Override
    public int hashCode() {
        return userVertex != null ? userVertex.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || userVertex == null) {
            return false;
        }
        if(!(obj instanceof Vertex)){
            return false;
        }
        return userVertex.equals(((Vertex<T>) obj).userVertex);
    }
}
