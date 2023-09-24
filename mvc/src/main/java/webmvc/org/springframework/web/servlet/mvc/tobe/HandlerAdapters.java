package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HandlerAdapters {

    private final Set<HandlerAdapter> handlers = new HashSet<>();

    public HandlerAdapters(List<HandlerAdapter> handlerAdapters) {
        handlers.addAll(handlerAdapters);
    }

    public Object execute(final Object handler, final HttpServletRequest req, final HttpServletResponse res) {
        for (HandlerAdapter handlerAdapter : handlers) {
            if (handlerAdapter.isSupport(handler)) {
                return handlerAdapter.execute(handler, req, res);
            }
        }

        throw new IllegalArgumentException("처리할 수 없는 요청입니다. handler가 맞지 않습니다.");
    }
}

