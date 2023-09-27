package webmvc.org.springframework.web.servlet.mvc.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JsonView;

public class HandlerExecution {

    private final Object handler;
    private final Method method;

    public HandlerExecution(final Object handler, final Method method) {
        this.handler = handler;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final Object result = method.invoke(handler, request, response);
        if (result instanceof ModelAndView) {
            return (ModelAndView) result;
        }
        if (result == null) {
            return new ModelAndView(new JsonView());
        }
        if (result instanceof String) {
            return new ModelAndView(result);
        }
        throw new HandlerException("해당 핸들러의 반환타입이 올바르지 않습니다.");
    }
}
