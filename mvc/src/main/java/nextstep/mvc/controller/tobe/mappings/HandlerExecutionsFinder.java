package nextstep.mvc.controller.tobe.mappings;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandlerExecutionsFinder {

    private static final Logger log = LoggerFactory.getLogger(HandlerExecutionsFinder.class);
    private static final int PARAMETER_MIN_LENGTH = 2;

    private final ControllerScanner scanner;

    public HandlerExecutionsFinder(ControllerScanner scanner) {
        this.scanner = scanner;
    }

    public Map<HandlerKey, HandlerExecution> findHandlerExecutions(String basePackage) {
        Map<HandlerKey, HandlerExecution> executions = new HashMap<>();
        Map<Class<?>, Object> instanceByClasses = scanner.createInstanceByClasses(basePackage);

        for (Entry<Class<?>, Object> entry : instanceByClasses.entrySet()) {
            Map<Method, RequestMapping> map = findRequestMappingAnnotatedMethods(entry.getKey());
            executions.putAll(mapToHandlerExecutionsPerMethod(map, entry.getValue()));
        }
        return executions;
    }

    private Map<Method, RequestMapping> findRequestMappingAnnotatedMethods(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toMap(
                        Function.identity(),
                        method -> method.getDeclaredAnnotation(RequestMapping.class)
                ));
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
                .peek(method -> log.info("Found annotated handler " + entry.getValue().value() +
                        " " + Arrays.toString(entry.getValue().method())))
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
