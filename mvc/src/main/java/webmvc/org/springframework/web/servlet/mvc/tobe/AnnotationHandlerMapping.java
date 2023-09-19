package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import webmvc.org.springframework.web.servlet.exception.HandlerMappingException;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
        initialize();
    }

    @Override
    public void initialize() {
        log.info("====================> Initialized AnnotationHandlerMapping!");
        new Reflections(basePackage)
                .getTypesAnnotatedWith(Controller.class)
                .forEach(this::putHandlerExecutions);
    }

    @Override
    public boolean support(final HttpServletRequest request) {
        return handlerExecutions.containsKey(getHandlerKey(request));
    }

    private void putHandlerExecutions(final Class<?> clazz) {
        final Object controller = getController(clazz);
        final List<Method> methods = getMethods(clazz);
        for (final Method method : methods) {
            final RequestMapping annotation = method.getDeclaredAnnotation(RequestMapping.class);
            final HandlerKey handlerKey = new HandlerKey(annotation.value(), annotation.method()[0]);
            final HandlerExecution handlerExecution = new HandlerExecution(controller, method);

            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private Object getController(final Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            log.warn("Controller 를 생성자로 생성하던 도중 예외가 발생하였습니다.", e);
            throw new HandlerMappingException("[ERROR] Controller 를 생성자로 생성하던 도중 예외가 발생하였습니다.");
        }
    }

    private List<Method> getMethods(final Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toList());
    }

    @Override
    public Object getHandlerExecution(final HttpServletRequest request) {
        final HandlerKey handlerKey = getHandlerKey(request);

        return handlerExecutions.get(handlerKey);
    }
}
