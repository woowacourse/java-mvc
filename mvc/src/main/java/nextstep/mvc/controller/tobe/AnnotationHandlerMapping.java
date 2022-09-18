package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        Reflections reflections = new Reflections(basePackage);
        List<Method> methods = reflections.getTypesAnnotatedWith(Controller.class).stream()
                .map(Class::getDeclaredMethods)
                .flatMap(Stream::of)
                .collect(Collectors.toList());
        for (Method method : methods) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMapping.method()[0]);
            handlerExecutions.put(handlerKey, new HandlerExecution(method));
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestMethod = request.getMethod();
        String uri = request.getRequestURI();

        HandlerKey handlerKey = new HandlerKey(uri, RequestMethod.from(requestMethod));
        return handlerExecutions.get(handlerKey);
    }
}
