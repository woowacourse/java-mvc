package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HandlerExecutors {

    private final Set<HandlerExecutor> handlers = new HashSet<>();

    public HandlerExecutors(List<HandlerExecutor> handlerExecutors) {
        handlers.addAll(handlerExecutors);
    }

    public Object execute(final Object handler, final HttpServletRequest req, final HttpServletResponse res) {
        for (HandlerExecutor handlerExecutor : handlers) {
            if (handlerExecutor.isSupport(handler)) {
                return handlerExecutor.execute(handler, req, res);
            }
        }

        throw new IllegalArgumentException("처리할 수 없는 요청입니다. handler가 맞지 않습니다.");
    }
}

