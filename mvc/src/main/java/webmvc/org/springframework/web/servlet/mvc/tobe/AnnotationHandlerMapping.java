package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        Set<Method> handlers = findControllerClassesFrom(basePackages).stream()
                .flatMap(it -> getHandlersIn(it).stream())
                .collect(Collectors.toSet());

        for (Method handler : handlers) {
            HandlerExecution handlerExecution = getHandlerExecutionFor(handler);

            Set<HandlerKey> handlerKeys = getHandlerKeysOf(handler);
            handlerKeys.forEach(key -> handlerExecutions.put(key, handlerExecution));

            handlerKeys.forEach(it -> log.info("mapped " + it));
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private Set<Class<?>> findControllerClassesFrom(Object[] basePackages) {
        return new Reflections(basePackages).getTypesAnnotatedWith(Controller.class);
    }

    private List<Method> getHandlersIn(Class<?> controller) {
        return Arrays.stream(controller.getDeclaredMethods())
                .filter(it -> it.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toList());
    }

    private Set<HandlerKey> getHandlerKeysOf(Method handler) {
        RequestMapping requestMapping = handler.getAnnotation(RequestMapping.class);

        return Arrays.stream(requestMapping.method())
                .map(requestMethod -> new HandlerKey(requestMapping.value(), requestMethod))
                .collect(Collectors.toSet());
    }

    private HandlerExecution getHandlerExecutionFor(Method handler) {
        return new ReflectiveHandlerExecution(
                safeInstantiate(handler.getDeclaringClass()),
                handler
        );
    }

    private Object safeInstantiate(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException exception) {
            throw new IllegalArgumentException("Class " + clazz.getSimpleName() + "에 기본 생성자가 없습니다");
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Class " + clazz.getSimpleName() + "초기화에 실패했습니다");
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        return handlerExecutions.get(new HandlerKey(
                request.getRequestURI(),
                RequestMethod.valueOf(request.getMethod())
        ));
    }

    @Override
    public boolean supports(HttpServletRequest request) {
        return handlerExecutions.containsKey(new HandlerKey(
                request.getRequestURI(),
                RequestMethod.valueOf(request.getMethod())
        ));
    }
}
