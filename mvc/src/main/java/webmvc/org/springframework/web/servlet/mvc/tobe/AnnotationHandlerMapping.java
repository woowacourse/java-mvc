package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        controllerClasses.forEach(this::findRequestMapping);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void findRequestMapping(final Class<?> aClass) {
        Arrays.stream(aClass.getDeclaredMethods()).forEach(method -> {
            final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            if (requestMapping != null) {
                putHandlerExecutions(method, requestMapping);
            }
        });
    }

    private void putHandlerExecutions(final Method method, final RequestMapping requestMapping) {
        try {
            final Class<?> declaringClass = method.getDeclaringClass();
            final Object object = declaringClass.getDeclaredConstructor().newInstance();
            putHandlerExecution(method, requestMapping, object);
        } catch (Exception e) {
            log.info("{} Cannot get Constructor", method);
        }
    }

    private void putHandlerExecution(final Method method, final RequestMapping requestMapping, final Object object) {
        for (RequestMethod requestMethod : requestMapping.method()) {
            HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
            handlerExecutions.put(handlerKey, new HandlerExecution(object, method));
            log.info("put {}", handlerKey);
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        log.debug("Request Mapping Uri : {}", request.getRequestURI());
        return handlerExecutions.get(handlerKey);
    }
}
