package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import org.reflections.Reflections;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toList;

public class HandlerExecutionFactory {

    public static Map<HandlerKey, HandlerExecution> create(final Object... basePackage) {
        final Map<HandlerKey, HandlerExecution> handlerExecutions = new HashMap<>();

        final Reflections reflections = new Reflections(basePackage);

        final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        for (final Class<?> controller : controllers) {

            final List<Method> methods = Arrays.stream(controller.getDeclaredMethods())
                    .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                    .collect(toList());

            for (final Method method : methods) {
                final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                final RequestMethod[] requestMethods = requestMapping.method();
                final String requestUrl = requestMapping.value();

                final Object controllerInstance = createControllerInstance(controller);
                final HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, method);

                for (final RequestMethod requestMethod : requestMethods) {
                    final HandlerKey handlerKey = new HandlerKey(requestUrl, requestMethod);
                    handlerExecutions.put(handlerKey, handlerExecution);
                }
            }
        }

        return handlerExecutions;
    }

    private static Object createControllerInstance(final Class<?> controller) {
        try {
            return controller.getDeclaredConstructor().newInstance();
        } catch (final NoSuchMethodException |
                       InvocationTargetException |
                       InstantiationException |
                       IllegalAccessException e) {

            throw new IllegalStateException("controllerInstance 를 생성할 수 없습니다.");
        }
    }

}
