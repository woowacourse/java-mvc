package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.mvc.reflection.ReflectiveHandlerExecutionAdapter;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;
    private final ControllerInstanceManager controllerInstanceManager = new ControllerInstanceManager();

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        try {
            for (var controllerClass : findControllerClassesFrom(basePackages)) {
                List<Method> handlers = getHandlersIn(controllerClass);

                Map<HandlerKey, HandlerExecution> newHandlerExecutions = handlers.stream()
                        .map(it -> getHandlerExecutionMapFor(it))
                        .flatMap(it -> it.entrySet().stream())
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

                handlerExecutions.putAll(newHandlerExecutions);
            }
        } catch (ClassNotFoundException e) {
            log.info("잘못된 basePackage가 있습니다");
            throw new IllegalArgumentException("잘못된 basePackage가 있습니다");
        } finally {
            log.info("Initialized AnnotationHandlerMapping!");
        }
    }

    private Set<Class<?>> findControllerClassesFrom(Object[] basePackages) throws ClassNotFoundException {
        return new Reflections(basePackages).getTypesAnnotatedWith(Controller.class);
    }

    private List<Method> getHandlersIn(Class<?> controller) {
        return Arrays.stream(controller.getDeclaredMethods())
                .filter(it -> it.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toList());
    }

    private Map<HandlerKey, HandlerExecution> getHandlerExecutionMapFor(Method handler) {
        Map<HandlerKey, HandlerExecution> handlerExecutionMap = new HashMap<>();
        RequestMapping requestMapping = handler.getAnnotation(RequestMapping.class);
        for (RequestMethod requestMethod : requestMapping.method()) {
            handlerExecutionMap.put(
                    new HandlerKey(requestMapping.value(), requestMethod),
                    getHandlerExecutionFor(handler)
            );
        }
        return handlerExecutionMap;
    }

    private ReflectiveHandlerExecutionAdapter getHandlerExecutionFor(Method handler) {
        return new ReflectiveHandlerExecutionAdapter(
                controllerInstanceManager.getInstanceOf(handler),
                handler
        );
    }

    public Object getHandler(final HttpServletRequest request) {
        return handlerExecutions.get(new HandlerKey(
                request.getRequestURI(),
                RequestMethod.valueOf(request.getMethod())
        ));
    }
}
