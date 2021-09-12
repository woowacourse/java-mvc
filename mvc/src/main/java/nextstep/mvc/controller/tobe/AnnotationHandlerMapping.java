package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");

        try {
            Reflections reflections = new Reflections(basePackage, new MethodAnnotationsScanner(), new TypeAnnotationsScanner(), new SubTypesScanner());
            for (Class<?> annotatedClass : reflections.getTypesAnnotatedWith(Controller.class)) {
                for (Method method : annotatedClass.getDeclaredMethods()) {
                    final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                    if (Objects.nonNull(requestMapping)) {
                        final HandlerKey handlerKey = new HandlerKey(requestMapping.value(), RequestMethod.valueOf(requestMapping.method()[0].name()));
                        handlerExecutions.put(handlerKey, new HandlerExecution(method.getDeclaringClass().getConstructor().newInstance(), method));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        HandlerKey key = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(key);
    }
}
