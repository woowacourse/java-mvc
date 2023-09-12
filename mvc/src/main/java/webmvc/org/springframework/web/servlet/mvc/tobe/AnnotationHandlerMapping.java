package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        log.info("Initialized AnnotationHandlerMapping!");
        ControllerScanner controllerScanner = new ControllerScanner(basePackages);
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        Set<Method> methods = getRequestMappingMethods(controllers.keySet());
        for (Method method : methods) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            addHandleExecutions(controllers, method, requestMapping);
        }
    }

    private void addHandleExecutions(Map<Class<?>, Object> controllers, Method method, RequestMapping requestMapping) {
        List<HandlerKey> handlerKeys = mapHandlerKey(requestMapping.value(), requestMapping.method());
        for (HandlerKey handlerKey : handlerKeys) {
            HandlerExecution handlerExecution = new HandlerExecution(controllers.get(method.getDeclaringClass()), method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private Set<Method> getRequestMappingMethods(Set<Class<?>> classes) {
        return classes.stream()
                .flatMap(clazz -> Arrays.stream(clazz.getMethods()))
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toSet());
    }

    private List<HandlerKey> mapHandlerKey(String path, RequestMethod[] methods) {
        return Arrays.stream(methods)
                .map(method -> new HandlerKey(path, method))
                .collect(Collectors.toList());
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());

        return handlerExecutions.get(new HandlerKey(requestURI, requestMethod));
    }
}
