package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
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

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        final Set<Class<?>> controllerClasses = new Reflections(basePackage).getTypesAnnotatedWith(Controller.class);
        for (final Class<?> controllerClass : controllerClasses) {
            saveControllerAnnotatedClass(controllerClass);
        }
    }

    private void saveControllerAnnotatedClass(final Class<?> controllerClass) {
        final List<Method> methods = getDeclaredMethodsInControllerClass(controllerClass);
        for (final Method method : methods) {
            saveHandlerByDeclaredMethods(method);
        }
    }

    private List<Method> getDeclaredMethodsInControllerClass(final Class<?> controllerClass) {
        return Arrays.stream(controllerClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toList());
    }

    private void saveHandlerByDeclaredMethods(final Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final String url = requestMapping.value();
        final RequestMethod[] requestMethods = requestMapping.method();
        for (RequestMethod requestMethod : requestMethods) {
            final HandlerKey handlerKey = new HandlerKey(url, requestMethod);
            handlerExecutions.put(handlerKey, new HandlerExecution(method));
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final String method = request.getMethod();
        final String requestURI = request.getRequestURI();
        final HandlerKey handlerKey = new HandlerKey(requestURI, RequestMethod.from(method));
        return handlerExecutions.get(handlerKey);
    }
}
