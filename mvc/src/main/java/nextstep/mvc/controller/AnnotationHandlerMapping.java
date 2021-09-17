package nextstep.mvc.controller;

import com.google.common.collect.Sets;
import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        Set<Method> methods = getRequestMappingMethods(controllers.keySet());
        initHandlerExecutions(controllers, methods);
    }

    private Set<Method> getRequestMappingMethods(Set<Class<?>> controllers) {
        Set<Method> requestMappingMethods = Sets.newHashSet();
        for (Class<?> controller : controllers) {
            requestMappingMethods.addAll(ReflectionUtils.getAllMethods(controller, ReflectionUtils.withAnnotation(RequestMapping.class)));
        }
        return requestMappingMethods;
    }

    private void initHandlerExecutions(Map<Class<?>, Object> controllers, Set<Method> methods) {
        for (Method method : methods) {
            RequestMapping rm = method.getAnnotation(RequestMapping.class);
            handlerExecutions.put(createHandlerKey(rm), createHandlerExecution(controllers, method));
        }
    }

    private HandlerKey createHandlerKey(RequestMapping rm) {
        return new HandlerKey(rm.value(), rm.method()[0]);
    }

    private HandlerExecution createHandlerExecution(Map<Class<?>, Object> controllers, Method method) {
        return new HandlerExecution(controllers.get(method.getDeclaringClass()), method);
    }

    public Object getHandler(HttpServletRequest request) {
        String uri = request.getRequestURI();
        RequestMethod method = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(uri, method));
    }
}
