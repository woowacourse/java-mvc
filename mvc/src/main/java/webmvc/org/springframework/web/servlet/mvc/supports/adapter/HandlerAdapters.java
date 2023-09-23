package webmvc.org.springframework.web.servlet.mvc.supports.adapter;

import java.util.ArrayList;
import java.util.List;
import webmvc.org.springframework.web.servlet.mvc.supports.HandlerAdapter;

public class HandlerAdapters {

    private final List<HandlerAdapter> adapter = new ArrayList<>();

    public void addHandlerAdapter(final HandlerAdapter targetHandlerAdapter) {
        adapter.add(targetHandlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(final Object handler) {
        for (final HandlerAdapter targetHandlerAdapter : adapter) {
            if (targetHandlerAdapter.supports(handler)) {
                return targetHandlerAdapter;
            }
        }

        throw new IllegalStateException("해당 Handler를 수행할 수 있는 HandlerAdapter가 존재하지 않습니다.");
    }
}
