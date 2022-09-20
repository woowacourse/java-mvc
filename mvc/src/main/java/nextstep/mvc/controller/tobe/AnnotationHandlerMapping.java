package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
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

    private static final Class<RequestMapping> REQUEST_MAPPING_CLASS = RequestMapping.class;
    private static final Class<Controller> CONTROLLER_CLASS = Controller.class;
    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        findControllers().stream()
                .map(this::findMappingMethods)
                .forEach(methods -> methods.forEach(this::saveMethodMapping));
    }

    private void saveMethodMapping(Method method) {
        RequestMapping annotation = method.getAnnotation(REQUEST_MAPPING_CLASS);
        for (RequestMethod requestMethod : annotation.method()) {
            String requestURL = annotation.value();
            HandlerKey handlerKey = new HandlerKey(requestURL, requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(method);
            handlerExecutions.put(handlerKey, handlerExecution);
            log.info("Path : {}, Method : {}.{}()", requestURL, method.getDeclaringClass(), method.getName());
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod method = RequestMethod.valueOf(request.getMethod());
        return handlerExecutions.get(new HandlerKey(requestURI, method));
    }

    public boolean sameRequestURL(String requestURL, Method classMethod) {
        return classMethod.getAnnotation(REQUEST_MAPPING_CLASS).value().equals(requestURL);
    }

    public boolean anyMatchRequestMethod(RequestMethod requestMethod, Method classMethod) {
        return Arrays.stream(classMethod.getAnnotation(REQUEST_MAPPING_CLASS).method())
                .anyMatch(httpMethod -> httpMethod == requestMethod);
    }

    public List<Class<?>> findControllers() {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(CONTROLLER_CLASS);
        return new ArrayList<>(controllers);
    }

    public List<Method> findMappingMethods(Class<?> controllerClass) {
        return Arrays.stream(controllerClass.getMethods())
                .filter(method -> method.isAnnotationPresent(REQUEST_MAPPING_CLASS))
                .collect(Collectors.toList());
    }
}
