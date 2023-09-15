package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        try {
            final List<Method> requestMethods = getRequestMethods();
            requestMethods.forEach(this::registerHandler);
            log.info("Initialized AnnotationHandlerMapping!");
        } catch (final Exception e) {
            log.error("Fail To Initialize AnnotationHandlerMapping : {}", e.getMessage());
        }
    }

    private List<Method> getRequestMethods() {
        final Reflections reflections = new Reflections(basePackages);
        return reflections.getTypesAnnotatedWith(Controller.class).stream()
                .map(Class::getDeclaredMethods)
                .flatMap(Arrays::stream)
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toList());
    }

    private void registerHandler(final Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final String requestURI = requestMapping.value();
        Arrays.stream(requestMapping.method())
                .forEach(requestMethod -> handlerExecutions.put(
                        new HandlerKey(requestURI, requestMethod),
                        new HandlerExecution(method)
                ));
    }

    public Object getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        final HandlerExecution handlerExecution = handlerExecutions.get(new HandlerKey(requestURI, requestMethod));
        if (handlerExecution == null) {
            throw new IllegalArgumentException("요청과 매핑되는 핸들러가 존재하지 않습니다.");
        }
        return handlerExecution;
    }
}
