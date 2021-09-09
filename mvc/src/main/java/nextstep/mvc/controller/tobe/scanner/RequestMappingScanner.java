package nextstep.mvc.controller.tobe.scanner;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.controller.tobe.HandlerKey;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;

public class RequestMappingScanner {

    private final Reflections reflections;

    public RequestMappingScanner(Reflections reflections) {
        this.reflections = reflections;
    }

    public Map<HandlerKey, HandlerExecution> getHandlerExecutions(Map<Class<?>, Object> controllers) {
        Map<HandlerKey, HandlerExecution> handlerExecutions = new HashMap<>();

        Set<Method> methodsWithRequestMapping = reflections.getMethodsAnnotatedWith(RequestMapping.class);
        for (Object methodWithRequestMapping : methodsWithRequestMapping.toArray()) {
            Method method = (Method) methodWithRequestMapping;
            Class<?> methodDeclaringClass = method.getDeclaringClass();

            RequestMapping annotation = method.getAnnotation(RequestMapping.class);
            String mappedURI = annotation.value();
            RequestMethod[] mappedMethods = annotation.method();

            for (RequestMethod mappedMethod : mappedMethods) {
                HandlerKey handlerKey = new HandlerKey(mappedURI, mappedMethod);
                HandlerExecution handlerExecution = new HandlerExecution(controllers.get(methodDeclaringClass), method);
                handlerExecutions.put(handlerKey, handlerExecution);
            }
        }

        return handlerExecutions;
    }
}
