package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> methodSpecificHandlers;
    private final Map<String, HandlerExecution> anyMethodHandlers;

    public AnnotationHandlerMapping(Object... basePackages) {
        this.basePackages = basePackages;
        this.methodSpecificHandlers = new HashMap<>();
        this.anyMethodHandlers = new HashMap<>();
    }

    public void initialize() {
        Reflections reflections = new Reflections(basePackages);
        Set<Class<?>> controllerTypes = reflections.getTypesAnnotatedWith(Controller.class);

        controllerTypes.stream()
                .map(this::instantiateController)
                .forEach(this::registerRequestMappings);

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private <T> T instantiateController(Class<T> controllerType) {
        try {
            return controllerType.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("컨트롤러 생성 실패: " + controllerType.getName(), e);
        }
    }

    private void registerRequestMappings(Object controllerInstance) {
        Set<Method> requestMappingMethods = ReflectionUtils.getAllMethods(
                controllerInstance.getClass(),
                ReflectionUtils.withAnnotation(RequestMapping.class)
        );

        requestMappingMethods.forEach(method -> registerRequestMapping(controllerInstance, method));
    }

    private void registerRequestMapping(Object controllerInstance, Method handlerMethod) {
        RequestMapping requestMapping = handlerMethod.getAnnotation(RequestMapping.class);
        String path = requestMapping.value();
        RequestMethod[] requestMethods = requestMapping.method();
        HandlerExecution execution = new HandlerExecution(controllerInstance, handlerMethod);

        boolean supportsAllMethods = requestMethods.length == 0;
        if (supportsAllMethods) {
            HandlerExecution existingHandler = anyMethodHandlers.putIfAbsent(path, execution);
            if (existingHandler != null) {
                throw new IllegalStateException("중복된 매핑: " + path);
            }
            return;
        }

        Arrays.stream(requestMethods)
                .map(requestMethod -> new HandlerKey(path, requestMethod))
                .forEach(handlerKey -> {
                    HandlerExecution existingHandler = methodSpecificHandlers.putIfAbsent(handlerKey, execution);
                    if (existingHandler != null) {
                        throw new IllegalStateException("중복된 매핑: " + handlerKey);
                    }
                });
    }

    public HandlerExecution getHandler(HttpServletRequest request) {
        String path = request.getRequestURI();

        Optional<HandlerExecution> methodSpecificHandler = RequestMethod.fromHttpMethod(request.getMethod())
                .map(requestMethod -> new HandlerKey(path, requestMethod))
                .map(methodSpecificHandlers::get);

        return methodSpecificHandler.orElseGet(() -> anyMethodHandlers.get(path));
    }
}
