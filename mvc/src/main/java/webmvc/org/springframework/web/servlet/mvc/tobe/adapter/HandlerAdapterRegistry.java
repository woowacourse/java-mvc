package webmvc.org.springframework.web.servlet.mvc.tobe.adapter;

import java.util.ArrayList;
import java.util.List;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdapter;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public HandlerAdapter findHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .findAny()
                .orElseThrow(() -> new IllegalStateException("핸들러를 처리하기 위한 어댑터가 존재하지 않습니다."));
    }

    public void addAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }
}
