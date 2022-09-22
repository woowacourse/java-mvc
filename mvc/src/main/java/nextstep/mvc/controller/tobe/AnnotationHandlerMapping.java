package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Class<RequestMapping> REQUEST_MAPPING_CLASS = RequestMapping.class;
    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        Reflections reflections = new Reflections(basePackage);
        ControllerScanner controllerScanner = new ControllerScanner(reflections);
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        Set<Method> mappingMethods = getRequestMappingMethods(controllers.keySet());
        for (Method mappingMethod : mappingMethods) {
            this.handlerExecutions.putAll(getHandlerExecutions(mappingMethod));
        }
    }

    private Set<Method> getRequestMappingMethods(Set<Class<?>> classes) {
        Set<Method> methods = new HashSet<>();
        for (Class<?> controllerClass : classes) {
            Predicate<Method> annotation = ReflectionUtils.withAnnotation(REQUEST_MAPPING_CLASS);
            Set<Method> allMethods = getAllMethods(controllerClass, annotation);
            methods.addAll(allMethods);
        }
        return methods;
    }

    @SafeVarargs
    private Set<Method> getAllMethods(Class<?> controllerClass, Predicate<? super Method>... predicates) {
        return ReflectionUtils.getAllMethods(controllerClass, predicates);
    }

    private Map<HandlerKey, HandlerExecution> getHandlerExecutions(Method method) {
        final HandlerExecution handlerExecution = new HandlerExecution(getInstance(method.getDeclaringClass()), method);
        final RequestMapping requestMapping = method.getAnnotation(REQUEST_MAPPING_CLASS);
        log.info("Path : {}, Method : {}", requestMapping.value(), requestMapping.method());

        return mapHandlerKeys(requestMapping.value(), requestMapping.method()).stream()
                .collect(Collectors.toMap(key -> key, key -> handlerExecution));
    }

    private List<HandlerKey> mapHandlerKeys(String path, RequestMethod[] requestMethods) {
        return Arrays.stream(requestMethods)
                .map(status -> new HandlerKey(path, status))
                .collect(Collectors.toList());
    }

    private Object getInstance(Class<?> aClass) {
        try {
            return aClass.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | SecurityException | InstantiationException |
                IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("인스턴스를 찾을 수 없습니다.");
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod method = RequestMethod.valueOf(request.getMethod());
        return handlerExecutions.get(new HandlerKey(requestURI, method));
    }
}
