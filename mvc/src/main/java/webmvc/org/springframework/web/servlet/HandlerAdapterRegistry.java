package webmvc.org.springframework.web.servlet;

import java.util.ArrayList;
import java.util.List;
import webmvc.org.springframework.web.servlet.exception.HandlerAdapterNotFoundException;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(Object object) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.support(object))
                .findFirst()
                .orElseThrow(() -> new HandlerAdapterNotFoundException("어댑터를 찾을 수 없습니다."));
    }
}
