package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
        Reflections reflections = new Reflections(basePackage);

        final var classes = new HashSet<Class<?>>();
        classes.addAll(reflections.getTypesAnnotatedWith(Controller.class));

        for (Class<?> aClass : classes) {
            try {
                Object controller = aClass.getConstructor().newInstance();
                Arrays.stream(aClass.getDeclaredMethods())
                        .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                        .forEach(method -> {
                            log.info(method.getName());
                            RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                            HandlerKey key = new HandlerKey(annotation.value(), annotation.method()[0]);
                            HandlerExecution value = new HandlerExecution();
                            handlerExecutions.put(key, value);
                        });
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(final HttpServletRequest request) {
        String uri = request.getRequestURI();
        RequestMethod method = RequestMethod.valueOf(request.getMethod());

        HandlerKey key = new HandlerKey(uri, method);

        return handlerExecutions.get(key);
    }
}
