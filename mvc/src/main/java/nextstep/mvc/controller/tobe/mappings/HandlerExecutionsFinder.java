package nextstep.mvc.controller.tobe.mappings;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import org.reflections.Reflections;

public class HandlerExecutionsFinder {

    private static final int PARAMETER_MIN_LENGTH = 2;

    public Map<HandlerKey, HandlerExecution> findHandlerExecutions(String basePackage) {
        Map<HandlerKey, HandlerExecution> executions = new HashMap<>();
        Set<Class<?>> classes = findControllerClass(basePackage);

        for (Class<?> clazz : classes) {
            Map<Method, RequestMapping> map = findRequestMappingAnnotatedMethods(clazz);
            Object instance = createInstance(clazz);
            executions.putAll(mapToHandlerExecutionsPerMethod(map, instance));
        }
        return executions;
    }

    private Set<Class<?>> findControllerClass(String basePackage) {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private Map<Method, RequestMapping> findRequestMappingAnnotatedMethods(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toMap(
                        Function.identity(),
                        method -> method.getDeclaredAnnotation(RequestMapping.class)
                ));
    }

    private static Object createInstance(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException |
                 IllegalAccessException |
                 InvocationTargetException |
                 NoSuchMethodException e) {
            throw new IllegalArgumentException("인스턴스 생성 시 오류가 발생했습니다", e);
        }
    }

    private Map<HandlerKey, HandlerExecution> mapToHandlerExecutionsPerMethod(Map<Method, RequestMapping> map,
                                                                              Object instance) {
        return map.entrySet()
                .stream()
                .filter(entry -> isSupportable(entry.getKey()))
                .map(entry -> mapToHandlerExecutionsPerMapping(entry, instance))
                .flatMap(partialMap -> partialMap.entrySet().stream())
                .collect(Collectors.toMap(
                        Entry::getKey,
                        Entry::getValue
                ));
    }

    private Map<HandlerKey, HandlerExecution> mapToHandlerExecutionsPerMapping(Entry<Method, RequestMapping> entry,
                                                                               Object instance) {
        return Arrays.stream(entry.getValue().method())
                .collect(Collectors.toMap(
                        method -> new HandlerKey(entry.getValue().value(), method),
                        method -> new HandlerExecution(instance, entry.getKey())
                ));
    }

    private boolean isSupportable(Method method) {
        return isReturnTypeSupportable(method) && isParameterTypesSupportable(method.getParameterTypes());
    }

    private boolean isReturnTypeSupportable(Method method) {
        return method.getReturnType() == ModelAndView.class;
    }

    private boolean isParameterTypesSupportable(Class<?>[] parameterTypes) {
        return parameterTypes.length >= PARAMETER_MIN_LENGTH &&
                parameterTypes[0] == HttpServletRequest.class &&
                parameterTypes[1] == HttpServletResponse.class;
    }
}
