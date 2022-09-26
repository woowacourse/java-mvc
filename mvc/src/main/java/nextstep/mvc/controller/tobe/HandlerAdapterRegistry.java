package nextstep.mvc.controller.tobe;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import nextstep.mvc.HandlerAdapter;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter findHandlerAdapter(final Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .findAny()
                .orElseThrow(() ->
                        new NoSuchElementException("어댑터 없음! handler = " + handler)
                );
    }
}
