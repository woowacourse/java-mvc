package nextstep.mvc.mapping;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.controller.tobe.HandlerKey;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        Map<Class<?>, Object> controllers = ControllerScanner.getControllers(basePackage);
        initializeHandlerExecutions(controllers);
    }

    private void initializeHandlerExecutions(Map<Class<?>, Object> controllers) {
        try {
            for (Map.Entry<Class<?>, Object> controllerEntry : controllers.entrySet()) {
                Class<?> controllerClass = controllerEntry.getKey();
                Set<Method> methods = ReflectionUtils.getAllMethods(controllerClass, ReflectionUtils.withAnnotation(RequestMapping.class));
                addHandlerExecutions(controllers, controllerClass, methods);
            }
        } catch (Exception e) {
            log.error("Annotation Handler Mapping Fail!", e);
        }
    }

    private void addHandlerExecutions(Map<Class<?>, Object> controllers, Class<?> controllerClass, Set<Method> methods) {
        for (Method method : methods) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            String url = requestMapping.value();
            RequestMethod[] requestMethods = requestMapping.method();
            List<HandlerKey> handlerKeys = getHandlerKeys(url, requestMethods);
            HandlerExecution handlerExecution = new HandlerExecution(controllers.get(controllerClass), method);
            for (HandlerKey handlerKey : handlerKeys) {
                this.handlerExecutions.put(handlerKey, handlerExecution);
                log.info("Path : {}, Controller : {}", handlerKey, controllerClass);
            }
        }
    }

    private List<HandlerKey> getHandlerKeys(String url, RequestMethod[] requestMethods) {
        return Arrays.stream(requestMethods)
                .map(requestMethod -> new HandlerKey(url, requestMethod))
                .collect(Collectors.toList());
    }

    public Object getHandler(HttpServletRequest request) {
        String uri = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        return handlerExecutions.get(new HandlerKey(uri, requestMethod));
    }
}
