package com.tiny.graphlib;

class SearchInfoItem<T> {
    private int label = Integer.MAX_VALUE;
    private boolean isVisited = false;
    private iVertex<T> prevVertex = null;

    public boolean getVisited() {
        return isVisited;
    }

    public void setVisited(boolean isVisited) {
        this.isVisited = isVisited;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public boolean labelCalculated() {
        return label != Integer.MAX_VALUE;
    }

    public iVertex<T> getPrevVertex() {
        return prevVertex;
    }

    public void setPrevVertex(iVertex<T> prevVertex) {
        this.prevVertex = prevVertex;
    }
}
