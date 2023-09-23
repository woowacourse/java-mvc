package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import web.org.springframework.web.bind.annotation.MethodMapping;
import webmvc.org.springframework.web.servlet.ModelAndView;

public class AnnotationHandlerAdapter implements HandlerAdaptor {
    @Override
    public boolean supports(final Object handler) {
        final Method method = (Method) handler;
        final Class<?> methodClass = method.getDeclaringClass();
        return methodClass.isAnnotationPresent(Controller.class) &&
                MethodMapping.isAnyMatchAnnotation(method.getAnnotations());
    }

    @Override
    public ModelAndView handle(
            final HandlerExecution handler,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) {
        try {
            return (ModelAndView) handler.handle(request, response);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new AnnotationMethodInvokeException("어노테이션 메서드 실행 도중 예외가 발생했습니다.", e);
        }
    }
}
