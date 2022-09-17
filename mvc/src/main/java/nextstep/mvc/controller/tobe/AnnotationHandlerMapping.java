package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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

    // TODO: 구현 필요
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> contollers = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> clazz : contollers) {
            Method[] controllerMethods = clazz.getDeclaredMethods();
            for (Method method : controllerMethods) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    String url = method.getAnnotation(RequestMapping.class).value();
                    RequestMethod requestMethod = method.getAnnotation(RequestMapping.class).method()[0];
                    HandlerKey handlerKey = new HandlerKey(url, requestMethod);
                    HandlerExecution handlerExecution = new HandlerExecution(clazz, method);
                    handlerExecutions.put(handlerKey, handlerExecution);
                }
            }
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);
        return handlerExecutions.get(handlerKey);
    }
}
