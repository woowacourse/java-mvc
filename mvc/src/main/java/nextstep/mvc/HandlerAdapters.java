package nextstep.mvc;

import nextstep.mvc.exception.NotFoundHandlerAdapterException;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapters {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapters() {
        this.handlerAdapters = new ArrayList<>();
    }

    public void add(HandlerAdapter handlerAdaptor) {
        this.handlerAdapters.add(handlerAdaptor);
    }

    public HandlerAdapter findHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .findAny()
                .orElseThrow(() -> new NotFoundHandlerAdapterException(handler));
    }
}
