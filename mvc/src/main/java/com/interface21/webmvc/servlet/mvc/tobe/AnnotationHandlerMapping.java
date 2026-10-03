package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    // 클래스 탐색 -> 객체 생성 -> 매핑 메서드 탐색 -> Map 등록
    public void initialize() {
        Set<Class<?>> classes = new HashSet<>();
        // 컨트롤러 클래스 탐색
        for (final var basePackage : this.basePackage) {
            Reflections reflections = new Reflections(basePackage);
            classes.addAll(reflections.getTypesAnnotatedWith(Controller.class));
        }

        for (final Class<?> clazz : classes) {
            try {
                // 객체 생성
                Object controller = clazz.getDeclaredConstructor().newInstance();

                // 매핑 메서드 탐색
                Method[] methods = clazz.getDeclaredMethods();

                for (final Method method : methods) {
                    if (method.isAnnotationPresent(RequestMapping.class)) {
                        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

                        String url = requestMapping.value();
                        RequestMethod[] requestMethods = requestMapping.method();

                        // Map 등록
                        if (requestMethods.length == 0) {
                            requestMethods = RequestMethod.values();
                        }

                        for (RequestMethod requestMethod : requestMethods) {
                            HandlerKey handlerKey = new HandlerKey(url, requestMethod);
                            HandlerExecution handlerExecution = new HandlerExecution(controller, method);
                            handlerExecutions.put(handlerKey, handlerExecution);
                        }
                    }
                }
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException(e);
            }
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    // 요청 정보 추출 -> 키 생성 -> Map 조회
    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());

        HandlerKey requestHandlerKey = new HandlerKey(requestURI, requestMethod);

        return handlerExecutions.get(requestHandlerKey);
    }
}
