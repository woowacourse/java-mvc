package nextstep.mvc.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import nextstep.mvc.HandlerMapping;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.controller.tobe.HandlerKey;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Class<RequestMapping> REQUEST_MAPPING_CLASS = RequestMapping.class;
    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        final Reflections reflections = new Reflections(basePackages);
        final ControllerScanner scanner = new ControllerScanner(reflections);

        for (Entry<Class<?>, Object> entry : scanner.getControllers().entrySet()) {
            final Set<Method> mappingMethods = getAllRequestMappingMethods(entry.getKey());
            this.handlerExecutions.putAll(getHandlerExecutionMap(entry.getValue(), mappingMethods));
        }
    }

    private Set<Method> getAllRequestMappingMethods(Class<?> clazz) {
        return getAllMethods(clazz, ReflectionUtils.withAnnotation(REQUEST_MAPPING_CLASS));
    }

    @SafeVarargs
    private Set<Method> getAllMethods(Class<?> controllerClass, Predicate<? super Method>... predicates) {
        return ReflectionUtils.getAllMethods(controllerClass, predicates);
    }

    private Map<HandlerKey, HandlerExecution> getHandlerExecutionMap(Object handler, Set<Method> mappingMethods) {
        return mappingMethods.stream()
                .map(method -> getHandlerExecutions(handler, method))
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    }

    private Map<HandlerKey, HandlerExecution> getHandlerExecutions(Object handler, Method method) {
        final HandlerExecution handlerExecution = new HandlerExecution(handler, method);
        final RequestMapping requestMapping = method.getAnnotation(REQUEST_MAPPING_CLASS);
        log.info("Path : {}, Method : {}", requestMapping.value(), requestMapping.method());

        return Arrays.stream(requestMapping.method())
                .map(status -> new HandlerKey(requestMapping.value(), status))
                .collect(Collectors.toMap(Function.identity(), key -> handlerExecution));
    }

    public Object getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final RequestMethod method = RequestMethod.valueOf(request.getMethod());
        return handlerExecutions.get(new HandlerKey(requestURI, method));
    }
}
