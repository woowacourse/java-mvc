package webmvc.org.springframework.web.servlet.mvc;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapters {

    private final List<HandlerAdapter> handlerAdapter = new ArrayList<>();

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        this.handlerAdapter.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(Object handler) {
        return handlerAdapter.stream()
                .filter(adapter -> adapter.isSupport(handler))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("handler adapter를 찾을 수 없습니다."));
    }
}
