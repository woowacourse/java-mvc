package nextstep.mvc.controller.tobe;

import java.util.ArrayList;
import java.util.List;
import nextstep.mvc.HandlerAdapter;

public class HandlerAdapters {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapters() {
        this.handlerAdapters = new ArrayList<>();
    }

    public void add(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
            .filter(handlerAdapter -> handlerAdapter.supports(handler))
            .findAny()
            .orElseThrow(IllegalStateException::new);
    }
}
