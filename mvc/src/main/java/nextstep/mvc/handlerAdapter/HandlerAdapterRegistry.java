package nextstep.mvc.handlerAdapter;

import java.util.ArrayList;
import java.util.List;

import nextstep.mvc.exception.ServletException;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public void addHandlerAdapter(HandlerAdapter adapter) {
        handlerAdapters.add(adapter);
    }

    public HandlerAdapter getHandlerAdapter(final Object handler) {
        return handlerAdapters.stream()
            .filter(handlerAdapter -> handlerAdapter.supports(handler))
            .findFirst()
            .orElseThrow(() -> new ServletException("Handler Adapter를 찾을 수 없습니다."));
    }
}
