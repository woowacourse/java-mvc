package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        Set<Method> methods = getRequestMappingMethods(controllers.keySet());
        methods.forEach(each -> addHandlerExecutions(controllers, each, each.getAnnotation(RequestMapping.class)));
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void addHandlerExecutions(Map<Class<?>, Object> controllers, Method method, RequestMapping requestMapping) {
        List<HandlerKey> handlerKeys = mapHandlerKeys(requestMapping.value(), requestMapping.method());
        handlerKeys.forEach(each ->
                handlerExecutions.put(each, new HandlerExecution(controllers.get(method.getDeclaringClass()), method)));
    }

    private Set<Method> getRequestMappingMethods(Set<Class<?>> classes) {
        return classes.stream()
                .flatMap(each -> Arrays.stream(each.getMethods())
                        .filter(method -> method.isAnnotationPresent(RequestMapping.class)))
                .collect(Collectors.toSet());
    }

    private List<HandlerKey> mapHandlerKeys(String url, RequestMethod[] requestMethods) {
        return Arrays.stream(requestMethods)
                .map(each -> new HandlerKey(url, each))
                .collect(Collectors.toUnmodifiableList());
    }

    public Object getHandler(HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        if (!this.handlerExecutions.containsKey(handlerKey)) {
            throw new IllegalArgumentException("request에 대응되는 메소드가 없습니다.");
        }
        return handlerExecutions.get(handlerKey);
    }
}
