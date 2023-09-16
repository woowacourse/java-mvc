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
        Method handlerMethod = (Method) handler;

        return handlerMethod.isAnnotationPresent(RequestMapping.class) ||
                isCustomRequestMappingPresent(handlerMethod);
    }

    private boolean isCustomRequestMappingPresent(Method handlerMethod) {
        Annotation[] annotations = handlerMethod.getDeclaredAnnotations();

        return Arrays.stream(annotations)
                .anyMatch(CustomRequestMappings::isAnyMatch);
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Object handlerInstance = instantiate(handler);
        Method handlerMethod = (Method) handler;

        try {
            return (ModelAndView) handlerMethod.invoke(handlerInstance, request, response);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private Object instantiate(Object handler) {
        try {
            Method handlerMethod = (Method) handler;
            return handlerMethod.getDeclaringClass()
                    .getDeclaredConstructor()
                    .newInstance();
        } catch (InstantiationException | IllegalAccessException |
                 InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
