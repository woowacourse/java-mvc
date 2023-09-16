package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.HandlerMapping;
import webmvc.org.springframework.web.servlet.ModelAndView;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        Set<Class<?>> controllers = findController();
        List<Method> methods = getMethods(controllers);
        methods.stream()
                .filter(this::isReturnType)
                .filter(this::isSameParameter)
                .forEach(this::addHandler);
    }

    private List<Method> getMethods(final Set<Class<?>> controllers) {
        return controllers.stream()
                .map(Class::getDeclaredMethods)
                .flatMap(Arrays::stream).collect(Collectors.toList());
    }

    private Set<Class<?>> findController() {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private boolean isReturnType(final Method method) {
        return method.getReturnType().equals(ModelAndView.class);
    }

    private boolean isSameParameter(final Method method) {
        List<Class<?>> parameterTypes = List.of(method.getParameterTypes());
        return parameterTypes.containsAll(List.of(HttpServletRequest.class, HttpServletResponse.class));
    }

    private void addHandler(final Method method) {
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);

        if (annotation != null) {
            for (RequestMethod requestMethod : annotation.method()) {
                HandlerKey handlerKey = new HandlerKey(annotation.value(), requestMethod);
                HandlerExecution handlerExecution = new HandlerExecution(method);
                handlerExecutions.put(handlerKey, handlerExecution);
            }
        }
    }

    public HandlerExecution getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        HandlerKey handlerKey = new HandlerKey(requestURI, RequestMethod.valueOf(method));

        return  handlerExecutions.get(handlerKey);
    }
}
