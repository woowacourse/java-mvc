package com.interface21.webmvc.servlet;

import java.util.Iterator;
import java.util.List;

public class HandlerAdapterRegistry implements Iterable<HandlerAdapter> {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegistry(final List<HandlerAdapter> handlerAdapters) {
        this.handlerAdapters = List.copyOf(handlerAdapters);
    }

    @Override
    public Iterator<HandlerAdapter> iterator() {
        return handlerAdapters.iterator();
    }
}
