package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.ReflectionUtils;
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

    @Override
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");

        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        for (Class<?> clazz : controllers.keySet()) {
            Set<Method> methods = getRequestMappingMethod(clazz);
            addAllHandlerExecutions(clazz, methods);
        }
    }

    private Set<Method> getRequestMappingMethod(Class<?> clazz) {
        return ReflectionUtils.getAllMethods(clazz,
                ReflectionUtils.withAnnotation(RequestMapping.class));
    }

    private void addAllHandlerExecutions(Class<?> clazz, Set<Method> methods) {
        for (Method method : methods) {
            RequestMapping annotation = method.getAnnotation(RequestMapping.class);
            String url = annotation.value();
            RequestMethod[] requestMethods = annotation.method();
            List<HandlerKey> handlerKeys = mapHandlerKeys(url, requestMethods);
            for (HandlerKey handlerKey : handlerKeys) {
                handlerExecutions.put(handlerKey, new HandlerExecution(clazz, method));
            }
        }
    }

    private List<HandlerKey> mapHandlerKeys(String url, RequestMethod[] requestMethods) {
        return Arrays.stream(requestMethods)
                .map(requestMethod -> new HandlerKey(url, requestMethod))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        String url = request.getRequestURI();
        String method = request.getMethod();
        RequestMethod requestMethod = RequestMethod.valueOf(method);
        return handlerExecutions.getOrDefault(new HandlerKey(url, requestMethod),
                handlerExecutions.get(new HandlerKey("/404", RequestMethod.GET)));
    }
}
