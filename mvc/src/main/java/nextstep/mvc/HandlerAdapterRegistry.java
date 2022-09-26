package nextstep.mvc;

import jakarta.servlet.ServletException;
import java.util.List;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegistry(final List<HandlerAdapter> handlerAdapters) {
        this.handlerAdapters = handlerAdapters;
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getAdapter(final Object controller) throws ServletException {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(controller))
                .findAny()
                .orElseThrow(() -> new ServletException("처리할 수 있는 handlerAdapter를 찾지 못했습니다."));
    }
}
