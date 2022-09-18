package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        Set<Class<?>> controllerClasses = getControllerClasses();

        for (Class<?> controllerClass : controllerClasses) {
            putToHandlerExecutions(controllerClass);
        }

        handlerExecutions.forEach(
                (handlerKey, handlerExecution) ->
                        log.info("Path : " + handlerKey.getRequestMethod() + " " + handlerKey.getUrl() +
                                "Controller : " + handlerExecution.getController().getClass()));
    }

    private Set<Class<?>> getControllerClasses() {
        final Set<Class<?>> controllerClasses = new HashSet<>();
        for (Object basePackage : basePackages) {
            final Reflections reflections = new Reflections(basePackage);
            controllerClasses.addAll(reflections.getTypesAnnotatedWith(Controller.class));
        }
        return controllerClasses;
    }

    private void putToHandlerExecutions(Class<?> controllerClass) {
        final Object controller = newInstanceOf(controllerClass);
        final Method[] declaredMethods = controllerClass.getDeclaredMethods();
        for (Method controllerMethod : declaredMethods) {
            final HandlerExecution handlerExecution = new HandlerExecution(controller, controllerMethod);

            final RequestMapping requestMapping = controllerMethod.getAnnotation(RequestMapping.class);
            final String url = requestMapping.value();
            final RequestMethod[] requestMethods = requestMapping.method();
            for (RequestMethod requestMethod : requestMethods) {
                final HandlerKey handlerKey = new HandlerKey(url, requestMethod);
                handlerExecutions.put(handlerKey, handlerExecution);
            }
        }
    }

    private Object newInstanceOf(Class<?> controllerClass) {
        try {
            final Constructor<?> constructor = controllerClass.getConstructor();
            return constructor.newInstance();
        } catch (Exception e) {
            log.error("no such constructor of Class");
            throw new RuntimeException();
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        return handlerExecutions.get(HandlerKey.from(request));
    }
}
