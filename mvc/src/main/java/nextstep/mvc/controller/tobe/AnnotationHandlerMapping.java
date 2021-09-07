package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.mvc.ClassScanner;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);
    private static final Class<Controller> CONTROLLER_CLASS = Controller.class;
    private static final Class<RequestMapping> REQUEST_MAPPING_CLASS = RequestMapping.class;

    private final ClassScanner classScanner;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.classScanner = new ClassScanner(basePackage);
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        final Set<Object> controllers = classScanner.scanAllObjectsWithAnnotation(CONTROLLER_CLASS);

        for (final Object controller : controllers) {
            final Set<Method> methods = classScanner.scanAllMethodsWithAnnotation(controller, REQUEST_MAPPING_CLASS);
            initializeFromMethods(methods, controller);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void initializeFromMethods(final Set<Method> methods, final Object controller) {
        for (final Method method : methods) {
            initializeFromMethod(method, controller);
        }
    }

    private void initializeFromMethod(final Method method, final Object controller) {
        final RequestMapping annotation = method.getAnnotation(REQUEST_MAPPING_CLASS);
        final String url = annotation.value();

        for (final RequestMethod requestMethod : annotation.method()) {
            handlerExecutions.put(new HandlerKey(url, requestMethod), new HandlerExecution(controller, method));
        }
    }

    public Object getHandler(HttpServletRequest request) {
        return handlerExecutions.get(new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod())));
    }
}
