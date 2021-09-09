package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.exeption.HandlerMappingException;
import nextstep.mvc.mapping.HandlerMapping;
import nextstep.mvc.scanner.HandlerScanner;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
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

    @Override
    public void initialize() {
        final HandlerScanner handlerScanner = new HandlerScanner(basePackage);
        for (Class<?> annotatedClass : handlerScanner.getHandler().keySet()) {
            for (Method declaredMethod : annotatedClass.getDeclaredMethods()) {
                putHandlerExecutionByRequestMapping(declaredMethod);
            }
        }
        handlerExecutions.forEach((key, value) -> log.info("Path: [{}], Controller: [{}], Method: [{}]", key.getUrl(), value.getHandler(), key.getRequestMethod()));
        log.info("Initialized AnnotationHandlerMapping!");
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        log.debug("Request Annotation Mapping Uri : {}", request.getRequestURI());
        return handlerExecutions.get(new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod())));
    }

    private void putHandlerExecutionByRequestMapping(Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if (Objects.nonNull(requestMapping)) {
            final HandlerKey handlerKey = new HandlerKey(requestMapping.value(), RequestMethod.valueOf(requestMapping.method()[0].name()));
            handlerExecutions.put(handlerKey, new HandlerExecution(getHandlerByAnnotatedMethod(method), method));
        }
    }

    private Object getHandlerByAnnotatedMethod(Method method) {
        try {
            return method.getDeclaringClass().getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.info("메소드 인스턴스화 예외입니다. 이유: {}", e.getMessage());
            throw new HandlerMappingException("메소드 인스턴스화 예외입니다. 이유: {}" + e.getMessage());
        }
    }
}
