package webmvc.org.springframework.web.servlet.mvc;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapters {

    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(adapter -> adapter.supports(handler))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("핸들러 어뎁터를 찾지 못했습니다.: " + handler));

    }
}
