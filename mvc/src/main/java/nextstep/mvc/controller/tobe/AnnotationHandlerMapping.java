package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        for (Object packageName : basePackage) {
            initializeAnnotationHandlerMapping(packageName);
        }
    }

    private void initializeAnnotationHandlerMapping(final Object packageName) {
        final Reflections reflections = new Reflections(packageName);
        final Set<Class<?>> classesWithController = extractClassesWithController(reflections);

        for (Class<?> aClass : classesWithController) {
            final List<Method> methodsWithRequestMapping = extractMethodsWithRequestMapping(aClass);
            for (Method method : methodsWithRequestMapping) {
                final Object handler = createInstance(aClass);
                initializeHandlerExecutions(handler, method);
            }
        }
    }

    private Set<Class<?>> extractClassesWithController(final Reflections reflections) {
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private List<Method> extractMethodsWithRequestMapping(final Class<?> aClass) {
        final Method[] methods = aClass.getMethods();
        return Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toUnmodifiableList());
    }

    private Object createInstance(final Class<?> aClass) {
        try {
            return aClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            return new RuntimeException("해당하는 클래스 정보로 인스턴스를 생성할 수 없습니다.");
        }
    }

    private void initializeHandlerExecutions(final Object handler, final Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final String value = requestMapping.value();
        final RequestMethod[] requestMethods = requestMapping.method();
        for (RequestMethod requestMethod : requestMethods) {
            final HandlerKey handlerKey = new HandlerKey(value, requestMethod);
            handlerExecutions.put(handlerKey, new HandlerExecution(handler, method));
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final String method = request.getMethod();
        final HandlerKey handlerKey = new HandlerKey(requestURI, RequestMethod.of(method));
        return handlerExecutions.get(handlerKey);
    }
}
