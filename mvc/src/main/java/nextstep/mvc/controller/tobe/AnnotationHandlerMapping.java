package nextstep.mvc.controller.tobe;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        Reflections reflections = new Reflections(basePackage);

        Map<String, ? extends Class<?>> controllers = reflections
            .getTypesAnnotatedWith(Controller.class).stream()
            .collect(toMap(Class::getName, controller -> controller));

        List<Method> requestMappingMethods = controllers.values().stream()
            .map(Class::getDeclaredMethods)
            .flatMap(Arrays::stream)
            .filter(method -> method.getDeclaredAnnotation(RequestMapping.class) != null)
            .collect(toList());

        requestMappingMethods.forEach(method -> {
            RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
            String value = requestMapping.value();
            RequestMethod[] requestMethods = requestMapping.method();

            addHandlerExecution(controllers, method, value, requestMethods);
        });

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void addHandlerExecution(Map<String, ? extends Class<?>> controllers, Method method, String value, RequestMethod[] requestMethods) {
        for (RequestMethod requestMethod: requestMethods) {
            HandlerKey handlerKey = new HandlerKey(value, requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(
                controllers.get(method.getDeclaringClass().getName()), method);

            handlerExecutions.put(handlerKey, handlerExecution);
            log.info("Path : {}, Controller : {}", value, method.getDeclaringClass().getSimpleName());
        }
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        HandlerKey handlerKey =
            new HandlerKey(
                request.getRequestURI(),
                RequestMethod.valueOf(request.getMethod())
            );

        return handlerExecutions.get(handlerKey);
    }
}
