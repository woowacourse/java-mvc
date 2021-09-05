package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
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
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        scanController(controllers);
    }

    @SuppressWarnings("unchecked")
    private void scanController(Set<Class<?>> controllers) {
        Set<Method> methods = new HashSet<>();
        for (Class<?> controller : controllers) {
            Set<Method> requestMappingMethods = ReflectionUtils.getAllMethods(controller, ReflectionUtils.withAnnotation(RequestMapping.class));
            methods.addAll(requestMappingMethods);
            initHandlerExecutions(methods, controller);
        }
    }

    private void initHandlerExecutions(Set<Method> methods, Class<?> controller) {
        for (Method method : methods) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            HandlerKey key = new HandlerKey(requestMapping.value(), requestMapping.method()[0]);
            initHandlerExecution(controller, method, key);
        }
    }

    private void initHandlerExecution(Class<?> controller, Method method, HandlerKey key) {
        try {
            HandlerExecution handlerExecution = new HandlerExecution(controller.getDeclaredConstructor().newInstance(), method);
            handlerExecutions.put(key, handlerExecution);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            log.error(e.getMessage());
        }
    }

    public Object getHandler(HttpServletRequest request) {
        String uri = request.getRequestURI();
        RequestMethod method = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(uri, method));
    }
}
