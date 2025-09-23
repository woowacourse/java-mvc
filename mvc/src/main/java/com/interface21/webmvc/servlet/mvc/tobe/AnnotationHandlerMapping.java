package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.core.util.ReflectionUtils;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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
        log.info("=== AnnotationHandlerMapping initialize start ===");

        // 1. basePackage 패키지 속 @Controller 어노테이션을 가진 클래스 탐색
        final Set<Class<?>> controllerClasses = new HashSet<>();
        for (final Object pkg : basePackage) {
            controllerClasses.addAll(ReflectionUtils.getTypesAnnotatedWith((String) pkg, Controller.class));
        }

        // 2. 컨트롤러 어노테이션을 가진 클래스를 handlerExecutions에 등록
        for (final Class<?> controllerClazz : controllerClasses) {
            try {
                final Object controllerInstance = controllerClazz.getDeclaredConstructor().newInstance();
                final Method[] methods = controllerClazz.getDeclaredMethods();
                for (final Method method : methods) {
                    if (!method.isAnnotationPresent(RequestMapping.class)) {
                        continue;
                    }

                    // 3. @RequestMapping 어노테이션이 붙은 자바 메서드만 처리
                    final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                    final String url = requestMapping.value();
                    RequestMethod[] httpMethods = requestMapping.method();
                    if (requestMapping.method().length == 0) {
                        httpMethods = RequestMethod.values();
                    }
                    for (final RequestMethod httpMethod : httpMethods) {
                        final HandlerKey handlerKey = new HandlerKey(url, httpMethod);
                        final HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, method);
                        handlerExecutions.put(handlerKey, handlerExecution);

                        log.info("Registered HandlerExecution: HTTP Method={}, URL={}, Controller={}.{}",
                                httpMethod, url, controllerClazz.getSimpleName(), method.getName());
                    }
                }
            } catch (Exception e) {
                log.error("Failed to initialize controller: {}", controllerClazz.getName(), e);
            }
        }
        log.info("=== AnnotationHandlerMapping initialize end ===");
    }

    public Object getHandler(final HttpServletRequest request) {
        return null;
    }
}
