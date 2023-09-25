package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.exception.JsonException;

public class AnnotationHandlerExecution implements HandlerExecution {

    private final Object controller;
    private final Method method;

    public AnnotationHandlerExecution(final Object controller, final Method method) {
        this.controller = controller;
        this.method = method;
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) {
        try {
            return (ModelAndView) method.invoke(controller, request, response);
        } catch (final InvocationTargetException e) {
            throw new JsonException(e.getTargetException().getMessage());
        } catch (final Exception e) {
            throw new IllegalArgumentException("[ERROR] 메서드를 실행하고, 객체로 캐스팅하는데 실패했습니다.");
        }
    }
}
