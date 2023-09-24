package webmvc.org.springframework.web.servlet.mvc;

import webmvc.org.springframework.web.servlet.mvc.exception.HandlerAdapterNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapterRegistry {

    private List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public HandlerAdapterRegistry() {
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        this.handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(final Object object) {
        return handlerAdapters.stream()
                              .filter(handlerAdapter -> handlerAdapter.supports(object))
                              .findAny()
                              .orElseThrow(HandlerAdapterNotFoundException::new);
    }
}
