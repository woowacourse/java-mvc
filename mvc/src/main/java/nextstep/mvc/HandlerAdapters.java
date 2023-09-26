package nextstep.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HandlerAdapters {

    private static final Logger log = LoggerFactory.getLogger(HandlerAdapters.class);

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapters(final List<HandlerAdapter> handlerAdapters) {
        this.handlerAdapters = new ArrayList<>(handlerAdapters);
    }

    public HandlerAdapters() {
        this(Collections.emptyList());
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        this.handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter findHandlerAdapter(final Object handler) {
        return handlerAdapters.stream()
                .filter(it -> it.supports(handler))
                .findAny()
                .orElseThrow(() -> new IllegalStateException("해당 handler를 지원하는 HandlerAdapter를 찾을 수 없습니다!"));
    }
}
