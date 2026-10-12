package com.interface21.webmvc.servlet;

import java.util.Iterator;
import java.util.List;

public class HandlerMappingRegistry implements Iterable<HandlerMapping> {

    private final List<HandlerMapping> handlerMappings;

    public HandlerMappingRegistry(final List<HandlerMapping> handlerMappings) {
        this.handlerMappings = List.copyOf(handlerMappings);
    }

    @Override
    public Iterator<HandlerMapping> iterator() {
        return handlerMappings.iterator();
    }
}
