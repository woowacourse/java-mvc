package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

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
            Reflections reflections = new Reflections(basePackage, new MethodAnnotationsScanner());
            for (Method method : reflections.getMethodsAnnotatedWith(RequestMapping.class)) {
                final RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                final HandlerKey handlerKey = new HandlerKey(annotation.value(), RequestMethod.valueOf(annotation.method()[0].name()));
                HandlerExecution handlerExecution = new HandlerExecution(method.getDeclaringClass().getConstructor().newInstance(), method);
                handlerExecutions.put(handlerKey, handlerExecution);
                log.debug("Initialize Key: {}, Execution: {}", handlerKey, handlerExecution);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object getHandler(HttpServletRequest request) {
        return null;
    }
}
