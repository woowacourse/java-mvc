package com.techcourse;

import com.interface21.webmvc.servlet.HandlerAdapter;
import java.util.List;

public class HandlerAdapterRegistry {
    List<HandlerAdapter> handlerAdapters;

    public void addHandlerAdapter(HandlerAdapter handlerAdapter){
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(Object handler) {

        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }
}
