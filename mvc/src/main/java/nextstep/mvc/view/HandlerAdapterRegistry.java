package nextstep.mvc.view;

import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.exception.NoSuchHandlerAdapterException;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegistry() {
        this.handlerAdapters = new ArrayList<>();
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        this.handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .findFirst().orElseThrow(() -> new NoSuchHandlerAdapterException("No such handler adapter"));
    }
}
