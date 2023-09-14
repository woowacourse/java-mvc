package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import org.reflections.Reflections;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HandlerExecutionFactory {

    public static Map<HandlerKey, HandlerExecution> create(final Object... basePackage) {
        final Map<HandlerKey, HandlerExecution> handlerExecutions = new HashMap<>();

        final Reflections reflections = new Reflections(basePackage);

        final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        for (final Class<?> controller : controllers) {

            for (final Method method : controller.getDeclaredMethods()) {
                if (method.isAnnotationPresent(RequestMapping.class)) {

                    final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                    final String requestUrl = requestMapping.value();
                    final RequestMethod[] requestMethods = requestMapping.method();

                    for (final RequestMethod requestMethod : requestMethods) {
                        final HandlerKey handlerKey = new HandlerKey(requestUrl, requestMethod);
                        final HandlerExecution handlerExecution = new HandlerExecution(controller, method);
                        handlerExecutions.put(handlerKey, handlerExecution);
                    }
                }
            }
        }

        return handlerExecutions;
    }

}
