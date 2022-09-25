package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
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
        Set<Class<?>> annotatedHandlers = ControllerScanner.getControllers(basePackage);
        for (Class<?> handler : annotatedHandlers) {
            initHandlerExecutionsFrom(handler);
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void initHandlerExecutionsFrom(final Class<?> handlerClassFile) {
        Object handler = ControllerScanner.instantiateControllers(handlerClassFile);
        List<Method> annotatedMethods = ControllerScanner.getMethods(handlerClassFile);

        for (Method method : annotatedMethods) {
            insertHandlerExecutionFrom(handler, method);
        }
    }

    private void insertHandlerExecutionFrom(final Object handler, final Method method) {
        RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
        String url = requestMapping.value();
        RequestMethod[] requestMethods = requestMapping.method();

        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(url, requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(handler, method);
            this.handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String url = request.getRequestURI();
        RequestMethod method = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(url, method);

        return handlerExecutions.get(handlerKey);
    }
}
