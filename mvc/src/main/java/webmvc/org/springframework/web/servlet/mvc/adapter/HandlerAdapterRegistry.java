package webmvc.org.springframework.web.servlet.mvc.adapter;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegistry() {
        this.handlerAdapters = new ArrayList<>();
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        this.handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(final Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.isSupportable(handler))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("핸들러를 처리할 수 있는 어댑터가 존재하지 않습니다."));
    }
}
