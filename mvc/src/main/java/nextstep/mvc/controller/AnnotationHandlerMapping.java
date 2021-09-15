package nextstep.mvc.controller;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.*;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final ControllerScanner controllerScanner;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.controllerScanner = new ControllerScanner(basePackage);
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        Set<Object> controllers = controllerScanner.getControllers();
        for (Object controller : controllers) {
            Set<Method> methods = methods(controller);
            methods.forEach(method -> putToHandlerExecutions(controller, method));
        }
    }

    public Set<Method> methods (Object controller) {
        Set<Method> methods = new HashSet<>();
        Method[] declaredMethods = controller.getClass().getDeclaredMethods();

        for (Method method : declaredMethods) {
            if (Arrays.stream(method.getDeclaredAnnotations())
                    .anyMatch(annotation -> annotation.annotationType().equals(RequestMapping.class)))
            {
                methods.add(method);
            }
        }
        return methods;
    }

    private void putToHandlerExecutions(Object controller, Method method) {
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        String uri = annotation.value();

        for (RequestMethod requestMethod : annotation.method()) {
            log.info("Path : {} {}, {} to Handler", requestMethod, uri, controller.getClass().getSimpleName());
            handlerExecutions.put(new HandlerKey(uri, requestMethod), new HandlerExecution(controller, method));
        }
    }

    public Object getHandler(HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}
