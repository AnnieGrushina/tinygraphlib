package com.tiny.graphlib;

import java.util.Map;

public interface iVertex<T> {
    Map<iVertex<T>, SearchInfoItem<T>> getSearchInfo();
    T getUserVertex();
}
