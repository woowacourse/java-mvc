package webmvc.org.springframework.web.servlet.mvc.tobe;

import java.util.ArrayList;
import java.util.List;

public class HandlerAdapterRegistry {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegistry() {
        this.handlerAdapters = new ArrayList<>();
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        this.handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(Object handler) {
        for (HandlerAdapter handlerAdapter : this.handlerAdapters) {
            if (handlerAdapter.isSupport(handler)) {
                return handlerAdapter;
            }
        }
        throw new IllegalArgumentException("해당 handler를 찾을 수 없습니다.");
    }
}
