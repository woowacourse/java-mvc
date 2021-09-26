package nextstep.mvc.mapping;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.handler.annotation.HandlerExecution;
import nextstep.mvc.handler.annotation.HandlerKey;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        List<Method> methods = scanMethods(controllers);

        for (Method method : methods) {
            instantiateHandlerExecutions(controllers, method);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private List<Method> scanMethods(Map<Class<?>, Object> controllers) {
        return controllers.keySet().stream()
                .map(clazz -> ReflectionUtils.getMethods(clazz, ReflectionUtils.withAnnotation(RequestMapping.class)))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private void instantiateHandlerExecutions(Map<Class<?>, Object> controllers, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        RequestMethod[] requestMethods = requestMapping.method();

        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(method, controllers.get(method.getDeclaringClass()));

            handlerExecutions.put(handlerKey, handlerExecution);
            log.info("created annotation based handler. controller: {}, name: {}, request http method: {}",
                    method.getDeclaringClass(), method.getName(), requestMethod);
        }
    }

    public Object getHandler(HttpServletRequest request) {
        return handlerExecutions.get(HandlerKey.of(request));
    }
}
