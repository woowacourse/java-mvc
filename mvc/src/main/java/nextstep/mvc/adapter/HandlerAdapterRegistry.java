package nextstep.mvc.adapter;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapterRegistry {

    private static final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    private HandlerAdapterRegistry() {
    }

    public static void addHandlerAdapters(HandlerAdapter adapter) {
        handlerAdapters.add(adapter);
    }

    public static HandlerAdapter getHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .findAny()
                .orElseThrow();
    }
}
