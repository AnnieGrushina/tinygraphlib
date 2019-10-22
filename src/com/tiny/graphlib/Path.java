package com.tiny.graphlib;

import java.util.List;

// this is a accessory class to provide human-readable path format
public class Path<T> {
    List<T> elements;

    public Path(List<T> elements) {
        this.elements = elements;
    }

    @Override
    public String toString() {
        if (elements == null) {
            return "Empty path";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < elements.size(); i++) {
            sb.append(elements.get(i));
            if (i < elements.size() - 1) {
                sb.append(" -> ");
            }
        }
        return sb.toString();
    }
}
