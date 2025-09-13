package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new ConcurrentHashMap<>();
    }

    /**
     * 컨트롤러 클래스들을 스캔해서 매핑을 등록하는 핵심 로직
     */
    public void initialize() {
        registerControllers(scanControllers());
        log.info("Initialized AnnotationHandlerMapping! size={}", handlerExecutions.size());
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey key = new HandlerKey(requestURI, requestMethod);
        return handlerExecutions.get(key);
    }

    private void registerControllers(Set<Class<?>> controllers) {
        controllers.forEach(this::registerController);
    }

    private Set<Class<?>> scanControllers() {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private void registerController(Class<?> controllerClass) {
        try {
            Object controllerInstance = controllerClass.getDeclaredConstructor().newInstance();
            for (Method method : controllerClass.getDeclaredMethods()) {
                registerHandlerMethod(controllerInstance, method);
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to initialize controller: " + controllerClass.getName(), e);
        }
    }

    private void registerHandlerMethod(Object controllerInstance, Method method) {
        RequestMapping mapping = method.getAnnotation(RequestMapping.class);
        if (mapping == null) {
            return;
        }

        for (RequestMethod requestMethod : resolveRequestMethods(mapping)) {
            HandlerKey handlerKey = new HandlerKey(mapping.value(), requestMethod);
            if (handlerExecutions.containsKey(handlerKey)) {
                throw new IllegalStateException("중복 매핑 발견: " + handlerKey);
            }
            handlerExecutions.put(handlerKey, new HandlerExecution(controllerInstance, method));
        }
    }

    private RequestMethod[] resolveRequestMethods(RequestMapping mapping) {
        RequestMethod[] methods = mapping.method();
        if (methods.length == 0) {
            // 모든 HTTP Method(GET, POST ...) 허용
            methods = RequestMethod.values();
        }
        return methods;
    }
}
