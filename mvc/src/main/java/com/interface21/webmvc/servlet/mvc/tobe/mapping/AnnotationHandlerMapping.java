package com.interface21.webmvc.servlet.mvc.tobe.mapping;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.tobe.util.ControllerScanner;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);
    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions = new HashMap<>();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    public void initialize() {
        log.info("스캔시작");
        ControllerScanner scanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> controllers = scanner.getControllers();
        registerControllers(controllers);
    }

    @Override
    public HandlerExecution getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey key = new HandlerKey(requestURI, requestMethod);
        return handlerExecutions.get(key);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private void registerControllers(Map<Class<?>, Object> controllers) {
        for (Map.Entry<Class<?>, Object> entry : controllers.entrySet()) {
            registerController(entry.getKey(), entry.getValue());
        }
    }

    private void registerController(Class<?> clazz, Object controllerInstance) {
        Set<Method> methods = ReflectionUtils.getAllMethods(
                clazz,
                ReflectionUtils.withAnnotation(RequestMapping.class)
        );
        registerHandlerMethods(controllerInstance, methods);
    }

    private void registerHandlerMethods(Object controllerInstance, Set<Method> methods) {
        for (Method method : methods) {
            log.info("메서드 등록 {}", method);
            registerHandlerMethod(controllerInstance, method);
        }
    }

    private void registerHandlerMethod(Object controllerInstance, Method method) {
        RequestMapping mapping = method.getAnnotation(RequestMapping.class);
        if (mapping == null) {
            return;
        }

        for (RequestMethod requestMethod : resolveRequestMethods(mapping)) {
            HandlerKey handlerKey = new HandlerKey(getPath(mapping), requestMethod);
            if (handlerExecutions.containsKey(handlerKey)) {
                throw new IllegalStateException("중복 매핑 발견: " + handlerKey);
            }
            handlerExecutions.put(handlerKey, new HandlerExecution(controllerInstance, method));
        }
    }

    private RequestMethod[] resolveRequestMethods(RequestMapping mapping) {
        RequestMethod[] methods = mapping.method();
        if (methods.length == 0) {
            // 모든 HTTP Method 허용
            methods = RequestMethod.values();
        }
        return methods;
    }

    private String getPath(RequestMapping mapping) {
        String path = mapping.value();
        if (path.isBlank()) {
            return "/";
        }
        return path;
    }
}
