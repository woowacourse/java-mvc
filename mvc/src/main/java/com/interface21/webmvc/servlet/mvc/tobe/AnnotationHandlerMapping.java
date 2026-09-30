package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        return RequestMethod.from(request.getMethod())
                .map(requestMethod -> new HandlerKey(request.getRequestURI(), requestMethod))
                .map(handlerExecutions::get)
                .orElse(null);
    }

    @Override
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        ControllerScanner scanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> controllers = scanner.getControllers();

        Set<Method> methods = getRequestMappingMethods(controllers.keySet());
        for (Method method : methods) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            addHandlerExecutions(controllers, method, requestMapping);
        }
    }

    private Set<Method> getRequestMappingMethods(final Set<Class<?>> controllerClasses) {
        return controllerClasses.stream()
                .flatMap(clazz ->
                        ReflectionUtils.getAllMethods(
                                clazz,
                                ReflectionUtils.withAnnotation(RequestMapping.class)
                        ).stream()
                ).collect(Collectors.toSet());
    }

    private void addHandlerExecutions(final Map<Class<?>, Object> controllers, final Method method, final RequestMapping requestMapping) {
        Class<?> declaringClass = method.getDeclaringClass();
        Object controller = controllers.get(declaringClass);
        String url = requestMapping.value();

        RequestMethod[] requestMethods = requestMapping.method();
        List<HandlerKey> handlerKeys = mapHandlerKeys(url, requestMethods);

        for (HandlerKey key : handlerKeys) {
            if (handlerExecutions.containsKey(key)) {
                throw new IllegalStateException(String.format("이미 존재하는 URL과 HTTP Method 매핑입니다: %s", key));
            }
            handlerExecutions.put(key, new HandlerExecution(controller, method));
        }
    }

    private List<HandlerKey> mapHandlerKeys(final String url, final RequestMethod[] requestMethods) {
        RequestMethod[] targets = requestMethods;
        if (requestMethods.length == 0) {
            targets = RequestMethod.values();
        }
        return Arrays.stream(targets).map(method -> new HandlerKey(url, method))
                .toList();
    }
}
