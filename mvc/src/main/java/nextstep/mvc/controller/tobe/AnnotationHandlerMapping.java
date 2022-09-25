package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
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
        log.info("Initialized AnnotationHandlerMapping!");
        Reflections reflections = new Reflections(basePackage);
        ControllerScanner controllerScanner = new ControllerScanner(reflections);

        Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        for (Class<?> controller : controllers.keySet()) {
            Set<Method> methodsWithAnnotation = getMethodsWithAnnotation(controller);
            addHandlerExecutions(controller, methodsWithAnnotation);
        }
    }

    private void addHandlerExecutions(Class<?> controller, Set<Method> methodsWithAnnotation) {
        for (Method method : methodsWithAnnotation) {
            RequestMapping requestMappingAnnotation = method.getDeclaredAnnotation(RequestMapping.class);
            String url = requestMappingAnnotation.value();
            RequestMethod[] requestMethods = requestMappingAnnotation.method();

            for (RequestMethod requestMethod : requestMethods) {
                putWithHandlerKey(method, url, requestMethod);
            }
        }
    }

    private void putWithHandlerKey(Method method, String url, RequestMethod requestMethod) {
        try {
            handlerExecutions.put(
                    new HandlerKey(url, requestMethod),
                    new HandlerExecution(method.getDeclaringClass().getConstructor().newInstance(), method)
            );
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private Set<Method> getMethodsWithAnnotation(Class<?> controller) {
        return ReflectionUtils
                .getAllMethods(controller, ReflectionUtils.withAnnotation(RequestMapping.class));
    }

    public Object getHandler(final HttpServletRequest request) {
        return handlerExecutions.get(
                new HandlerKey(
                        request.getRequestURI(), RequestMethod.valueOf(request.getMethod())
                )
        );
    }
}
