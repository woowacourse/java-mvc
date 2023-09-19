package webmvc.org.springframework.web.servlet.mvc.tobe.handler;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerKey;

public class AnnotationHandlerMapper implements HandlerMapper {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapper.class);

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapper(final Object... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        try {
            initHandlerExecutions();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void initHandlerExecutions() throws Exception {
        Reflections reflections = new Reflections(basePackages);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> controller : controllers) {
            Set<Method> methods = getRequestMappingMethods(controller);
            putHandlerExecutionsForAllMethod(controller, methods);
        }
    }

    private Set<Method> getRequestMappingMethods(final Class<?> controller) {
        return Arrays.stream(controller.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toSet());
    }

    private void putHandlerExecutionsForAllMethod(final Class<?> controller, final Set<Method> methods) throws Exception {
        Object handler = controller.getDeclaredConstructor().newInstance();
        for (Method method : methods) {
            putHandlerExecutionsForMethod(handler, method);
        }
    }

    private void putHandlerExecutionsForMethod(final Object handler, final Method method) {
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        String url = annotation.value();
        for (RequestMethod requestMethod : annotation.method()) {
            HandlerKey handlerKey = new HandlerKey(url, requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(handler, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        String methodName = request.getMethod();
        RequestMethod requestMethod = RequestMethod.findRequestMethod(methodName);
        String url = request.getRequestURI();
        HandlerKey handlerKey = new HandlerKey(url, requestMethod);
        return handlerExecutions.get(handlerKey);
    }
}
