package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
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

    private static final int EMPTY = 0;

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        for (Object packageName : basePackage) {
            extractClass(packageName);
        }
    }

    private void extractClass(final Object packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> clazz : classes) {
            setHandlerExecutions(clazz);
        }
    }

    private void setHandlerExecutions(final Class<?> clazz) {
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            addHandlerExecution(declaredMethod.getDeclaredAnnotation(RequestMapping.class));
        }
    }

    private void addHandlerExecution(final RequestMapping requestMapping) {
        String uri = requestMapping.value();
        RequestMethod[] method = requestMapping.method();
        validateRequestMethod(method);
        handlerExecutions.put(new HandlerKey(uri, method[0]), new HandlerExecution());
    }

    private static void validateRequestMethod(final RequestMethod[] method) {
        if (method.length == EMPTY) {
            log.info("fail initialize because method not registered");
            throw new IllegalArgumentException("요청 메서드가 존재하지 않습니다.");
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        return handlerExecutions.get(new HandlerKey(requestURI, RequestMethod.valueOf(method)));
    }
}
