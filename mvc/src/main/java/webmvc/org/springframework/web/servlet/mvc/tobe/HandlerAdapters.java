package webmvc.org.springframework.web.servlet.mvc.tobe;

import java.util.HashSet;
import java.util.Set;

public class HandlerAdapters {

    private final Set<HandlerAdapter> handlerAdapters;
	
    public HandlerAdapters() {
        this.handlerAdapters = new HashSet<>();
    }

    public void add(HandlerAdapter handlerAdapter) {
        this.handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(final Object handler) {
        return handlerAdapters.stream()
            .filter(handlerAdapter -> handlerAdapter.supports(handler))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("알맞는 HandlerAdapter가 없습니다."));
    }
}
