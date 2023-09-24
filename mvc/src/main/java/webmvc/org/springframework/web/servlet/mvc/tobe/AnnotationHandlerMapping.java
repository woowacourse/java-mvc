package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

public class AnnotationHandlerMapping implements HandlerMapping{

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, AnnotationHandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        Set<Class<?>> controllers = getAnnotatedControllerClasses();
        for (Class<?> controller : controllers) {
            addAnnotatedHandlerExecution(controller);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private Set<Class<?>> getAnnotatedControllerClasses() {
        Reflections reflections = new Reflections(basePackage);
        return new HashSet<>(reflections.getTypesAnnotatedWith(Controller.class));
    }

    private void addAnnotatedHandlerExecution(Class<?> controller) {
        for (Method method : getRequestMappedMethods(controller)) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            HandlerKey handlerKey = new HandlerKey(requestMapping.value(),
                    requestMapping.method()[0]);
            AnnotationHandlerExecution annotationHandlerExecution = new AnnotationHandlerExecution(
                    controller, method);
            handlerExecutions.put(handlerKey, annotationHandlerExecution);
        }
    }

    private List<Method> getRequestMappedMethods(Class<?> controller) {
        return Arrays.stream(controller.getMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toList());
    }

    @Override
    public HandlerExecution getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        return handlerExecutions.get(new HandlerKey(requestURI, requestMethod));
    }
}
