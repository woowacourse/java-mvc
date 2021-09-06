package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import nextstep.mvc.exeption.HandlerMappingException;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
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
        final Reflections reflections = new Reflections(basePackage, new MethodAnnotationsScanner(), new TypeAnnotationsScanner(), new SubTypesScanner());
        for (Class<?> annotatedClass : reflections.getTypesAnnotatedWith(Controller.class)) {
            for (Method declaredMethod : annotatedClass.getDeclaredMethods()) {
                putHandlerExecutionByRequestMapping(declaredMethod);
            }
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void putHandlerExecutionByRequestMapping(Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if (Objects.nonNull(requestMapping)) {
            final HandlerKey handlerKey = new HandlerKey(requestMapping.value(), RequestMethod.valueOf(requestMapping.method()[0].name()));
            handlerExecutions.put(handlerKey, new HandlerExecution(getHandlerByAnnotatedMethod(method), method));
        }
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        return handlerExecutions.get(new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod())));
    }

    private Object getHandlerByAnnotatedMethod(Method method) {
        try {
            return method.getDeclaringClass().getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.info("핸들러 매핑 예외입니다. 이유: {}", e.getMessage());
            throw new HandlerMappingException("핸들러 매핑 예외입니다. 이유: " + e.getMessage());
        }
    }
}
