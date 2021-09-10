package nextstep.mvc.mapping;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import nextstep.mvc.controller.tobe.ControllerScanner;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.controller.tobe.HandlerKey;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        Set<Object> controllers = ControllerScanner.scan(basePackage);
        for (Object controller : controllers) {
            Set<Method> methods = getMethods(controller);
            putRequestMethods(controller, methods);
        }
    }

    private Set<Method> getMethods(Object controller) {
        Set<Method> methods = new HashSet<>();
        Method[] declaredMethods = controller.getClass().getDeclaredMethods();
        for (Method method : declaredMethods) {
            boolean annotationMatching = Arrays.stream(method.getDeclaredAnnotations())
                    .anyMatch(annotation -> annotation.annotationType()
                            .equals(RequestMapping.class));
            if (annotationMatching) {
                methods.add(method);
            }
        }
        return methods;
    }

    private void putRequestMethods(Object controller, Set<Method> methods) {
        for (Method method : methods) {
            putHandlerExecutions(controller, method);
        }
    }

    private void putHandlerExecutions(Object controller, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        String uri = requestMapping.value();

        for (RequestMethod requestMethod : requestMapping.method()) {
            handlerExecutions.put(
                    new HandlerKey(uri, requestMethod),
                    new HandlerExecution(controller, method)
            );
        }
    }

    public Object getHandler(HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(),
                RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}
