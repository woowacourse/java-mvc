package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
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

    public void initialize() {
        Map<Class<?>, Object> controllers = ControllerScanner.findAllControllers();

        Set<Method> methods = getAllMethods(controllers);

        for (Method method : methods) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMapping.method());
            HandlerExecution handlerExecution = new HandlerExecution(controllers.get(method.getDeclaringClass()), method);
            handlerExecutions.put(handlerKey, handlerExecution);

        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private Set<Method> getAllMethods(Map<Class<?>, Object> allControllers) {
        Set<Method> methods = new HashSet<>();
        for (Class<?> controller : allControllers.keySet()) {
            methods.addAll(ReflectionUtils.getAllMethods(controller,
                    ReflectionUtils.withAnnotation(RequestMapping.class)));
        }
        return methods;
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.find(request.getMethod().toUpperCase(Locale.ROOT));
        return handlerExecutions.get(new HandlerKey(requestUri, requestMethod));
    }
}
