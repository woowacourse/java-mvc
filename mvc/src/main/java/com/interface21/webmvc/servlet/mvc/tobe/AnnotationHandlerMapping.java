package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        final Reflections reflections = new Reflections(basePackage);
        reflections.getTypesAnnotatedWith(Controller.class).forEach(this::registerHandlers);

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void registerHandlers(final Class<?> controllerClass) {
        final Object controller = createInstance(controllerClass);

        Arrays.stream(controllerClass.getMethods())
                .filter(controllerMethod -> controllerMethod.isAnnotationPresent(RequestMapping.class))
                .forEach(controllerMethod -> {
                    final RequestMapping mapping = controllerMethod.getAnnotation(RequestMapping.class);
                    RequestMethod[] requestHttpMethods = mapping.method();

                    if (requestHttpMethods.length == 0) {
                        requestHttpMethods = RequestMethod.values();
                    }

                    Arrays.stream(requestHttpMethods)
                            .map(requestHttpMethod -> new HandlerKey(mapping.value(), requestHttpMethod))
                            .forEach(handlerKey -> {
                                final HandlerExecution handlerExecution = new HandlerExecution(controller, controllerMethod);
                                if (handlerExecutions.putIfAbsent(handlerKey, handlerExecution) != null) {
                                    throw new IllegalStateException("이미 등록된 핸들러입니다: " + handlerKey);
                                }
                            });
                });
    }

    private Object createInstance(final Class<?> controllerClass) {
        try {
            return controllerClass.getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("컨트롤러 객체를 생성할 수 없습니다: " + controllerClass.getName(), e);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        return handlerExecutions.get(new HandlerKey(request.getRequestURI(),
                RequestMethod.valueOf(request.getMethod())));
    }
}
