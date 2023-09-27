package webmvc.org.springframework.web.servlet.mvc.adapter;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapters {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapters(final List<HandlerAdapter> handlerAdapters) {
        this.handlerAdapters = new ArrayList<>(handlerAdapters);
    }

    public HandlerAdapter getHandlerAdapter(final Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .findFirst()
                .orElseThrow(() -> new HandlerAdapterException("해당 요청에 대한 핸들러 어댑터가 없습니다."));
    }
}
