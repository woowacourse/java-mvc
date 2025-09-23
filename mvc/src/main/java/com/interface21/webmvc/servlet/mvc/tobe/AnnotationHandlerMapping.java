package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpServletRequest;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        for (Object pkg : basePackage) {
            ControllerScanner scanner = new ControllerScanner(pkg.toString());
            Map<Class<?>, Object> controllers = scanner.getControllers();
            Set<Method> methods = getRequestMappingMethods(controllers.keySet());
            for (Method method : methods) {
                addHandlerExecutions(controllers, method, method.getAnnotation(RequestMapping.class));
            }
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private Set<Method> getRequestMappingMethods(Set<Class<?>> controllers) {
        Set<Method> methods = new HashSet<>();
        for (Class<?> controllerClass : controllers) {
            try {
                methods.addAll(
                    ReflectionUtils.getAllMethods(controllerClass,
                        ReflectionUtils.withAnnotation(RequestMapping.class)
                    ));
            } catch (Exception e) {
                log.error("컨트롤러 메서드 조회 실패: {}", controllerClass.getName(), e);
            }
        }
        return methods;
    }

    private void addHandlerExecutions(Map<Class<?>, Object> controllers, Method method, RequestMapping requestMapping) {
        String requestUri = requestMapping.value();
        RequestMethod[] methods = requestMapping.method();
        for (HandlerKey key : mapHandlerKeys(requestUri, methods)) {
            handlerExecutions.put(key, new HandlerExecution(controllers.get(method.getDeclaringClass()), method));
            log.info("Mapped {} {} to {}", key.getRequestMethod(), key.getUrl(), method);
        }
    }

    private List<HandlerKey> mapHandlerKeys(String requestUri, RequestMethod[] methods) {
        return Arrays.stream(methods)
            .map(method -> new HandlerKey(requestUri, method))
            .toList();
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        String method = request.getMethod();
        HandlerKey handlerKey = new HandlerKey(requestUri, RequestMethod.from(method));
        HandlerExecution handlerExecution = handlerExecutions.get(handlerKey);
        if (handlerExecution == null) {
            return null;
        }
        return handlerExecution;
    }
}
