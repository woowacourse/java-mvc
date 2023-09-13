package webmvc.org.springframework.web.servlet.mvc.tobe;

import core.org.springframework.util.ReflectionUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.ModelAndView;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Object instance;

    public HandlerExecution(Object instance) {
        this.instance = instance;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        Class<?> clazz = instance.getClass();
        Method[] methods = clazz.getDeclaredMethods();

        String requestURI1 = request.getRequestURI();
        String requestMethod = request.getMethod();

        for (Method method : methods) {
            RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);

            String requestURI = requestMapping.value();
            RequestMethod[] requestMethods = requestMapping.method();

            for (RequestMethod each : requestMethods) {
                if (requestURI1.equals(requestURI) && each.equals(RequestMethod.valueOf(requestMethod))) {
                    Constructor<?> constructor = ReflectionUtils.accessibleConstructor(clazz);
                    ReflectionUtils.makeAccessible(constructor);
                    Object rawInstance = constructor.newInstance(null);

                    return (ModelAndView) method.invoke(rawInstance, request, response);
                }
            }
        }
        throw new IllegalArgumentException();
    }
}
