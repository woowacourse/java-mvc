package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

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

        Set<Class<?>> controllers = new HashSet<>(reflections.getTypesAnnotatedWith(Controller.class));

        for (Class<?> controller : controllers) {
            List<Method> controllerMethods = Arrays.stream(controller.getDeclaredMethods())
                    .filter(it -> it.isAnnotationPresent(RequestMapping.class))
                    .collect(Collectors.toList());

            try {
                addHandlers(controller.getDeclaredConstructor().newInstance(), controllerMethods);
            } catch (Exception e) {
                throw new IllegalStateException("cannot reflect controller instance");
            }
        }
    }

    private void addHandlers(Object controller, List<Method> controllerMethods) {
        for (Method method : controllerMethods) {
            String uri = method.getDeclaredAnnotation(RequestMapping.class).value();
            RequestMethod[] httpMethods = method.getDeclaredAnnotation(RequestMapping.class).method();
            for (RequestMethod httpMethod : httpMethods) {
                HandlerKey handlerKey = new HandlerKey(uri, httpMethod);
                HandlerExecution handlerExecution = new HandlerExecution(controller, method);
                handlerExecutions.put(handlerKey, handlerExecution);
            }
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String uri = request.getRequestURI();
        RequestMethod httpMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(uri, httpMethod);
        return handlerExecutions.get(handlerKey);
    }
}
