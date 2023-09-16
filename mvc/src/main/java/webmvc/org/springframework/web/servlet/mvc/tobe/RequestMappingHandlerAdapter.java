package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.web.bind.annotation.CustomRequestMappings;
import web.org.springframework.web.bind.annotation.RequestMapping;
import webmvc.org.springframework.web.servlet.ModelAndView;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class RequestMappingHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        final Method method = (Method) handler;
        final Class<?> handlerClass = method.getDeclaringClass();

        return handlerClass.isAnnotationPresent(RequestMapping.class) ||
                isCustomRequestMappingPresent(method);
    }

    private boolean isCustomRequestMappingPresent(Method handlerMethod) {
        Annotation[] annotations = handlerMethod.getDeclaredAnnotations();

        return Arrays.stream(annotations)
                .anyMatch(CustomRequestMappings::isAnyMatch);
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Object bean = handlerMethod.getBean();
        Method method = (Method) handlerMethod.getHandler();

        try {
            return (ModelAndView) method.invoke(bean, request, response);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
