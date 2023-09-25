package web.org.springframework.web;

import java.util.ArrayList;
import java.util.List;
import webmvc.org.springframework.web.servlet.mvc.handlermapping.HandlerAdapter;

public class HandlerAdapters {

    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public void add(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter findHandlerAdapter(Handler handler) {
        return handlerAdapters.stream()
                              .filter(adapter -> adapter.supports(handler))
                              .findFirst()
                              .orElseThrow(() -> new IllegalArgumentException("handlerAdapter를 찾을 수 없습니다. handler: " + handler));
    }
}
