package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import webmvc.org.springframework.web.servlet.ModelAndView;

public class HandlerExecutorComposite implements HandlerExecutor {

    private final Set<HandlerExecutor> handlerExecutors = new HashSet<>();

    public HandlerExecutorComposite(HandlerExecutor... handlerExecutors) {
        this.handlerExecutors.add(new RequestMappingAnnotationHandlerExecutor());
        this.handlerExecutors.addAll(Arrays.asList(handlerExecutors));
    }

    @Override
    public boolean executable(Object handler) {
        for (HandlerExecutor handlerExecutor : handlerExecutors) {
            if (handlerExecutor.executable(handler)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        for (HandlerExecutor handlerExecutor : handlerExecutors) {
            if (handlerExecutor.executable(handler)) {
                return handlerExecutor.handle(request, response, handler);
            }
        }
        return null;
    }
}
