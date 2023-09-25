package webmvc.org.springframework.web.servlet.adapter;

import java.util.HashSet;
import java.util.Set;

public class HandlerAdapters {

    private final Set<HandlerAdapter> handlerAdapters;

    public HandlerAdapters() {
        this.handlerAdapters = new HashSet<>();
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(final Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(String.format("%s를 실행시킬 어댑터를 찾지 못했습니다.", handler)));
    }
}
