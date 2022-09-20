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
        Set<Class<?>> annotatedHandlers = ControllerScanner.findAnnotatedController(basePackage);
        for (Class<?> handler : annotatedHandlers) {
            initHandlerExecutionsFrom(handler);
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void initHandlerExecutionsFrom(final Class<?> handlerClassFile) {
        List<Method> annotatedMethods = ControllerScanner.findAnnotatedMethod(handlerClassFile);
        for (Method method : annotatedMethods) {
            insertHandlerExecutionFrom(handlerClassFile, method);
        }
    }

    private void insertHandlerExecutionFrom(final Class<?> handlerClassFile, final Method method) {
        RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
        String url = requestMapping.value();
        RequestMethod[] requestMethods = requestMapping.method();

        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(url, requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(handlerClassFile, method);
            this.handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        return null;
    }
}
