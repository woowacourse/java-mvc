package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() throws Exception {
        log.info("Initialized AnnotationHandlerMapping!");

        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> controller : controllers) {
            Method[] methods = controller.getMethods();
            Object object = controller.getDeclaredConstructor().newInstance();

            generateHandlerExecutions(methods, object);
        }
    }

    private void generateHandlerExecutions(Method[] methods, Object object) {
        for (Method method : methods) {
            RequestMapping requestMappingMethod = method.getAnnotation(RequestMapping.class);

            if (requestMappingMethod != null) {
                String value = requestMappingMethod.value();
                RequestMethod[] requestMethods = requestMappingMethod.method();
                for (RequestMethod requestMethod : requestMethods) {

                    HandlerKey handlerKey = new HandlerKey(value, requestMethod);

                    HandlerExecution handlerExecution = new HandlerExecution(method, object);

                    handlerExecutions.put(handlerKey, handlerExecution);
                }
            }
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        RequestMethod requestMethod = RequestMethod.valueOf(method);

        HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);

        return handlerExecutions.get(handlerKey);
    }
}
