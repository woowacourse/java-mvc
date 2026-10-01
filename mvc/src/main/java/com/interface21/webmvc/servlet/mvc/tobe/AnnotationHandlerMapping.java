package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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
        Reflections reflections = new Reflections(basePackage);
        log.info("핸들러 매핑 초기화 시작: 탐색 패키지={}", Arrays.toString(basePackage));

        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> clazz : typesAnnotatedWith) {
            Object controller;
            try {
                controller = clazz.getDeclaredConstructor().newInstance();
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException("컨트롤러 인스턴스 생성 실패: " + clazz.getName(), e);
            }

            Method[] declaredMethods = clazz.getDeclaredMethods();
            log.info("컨트롤러 탐색: 클래스={}, 선언된 메서드 {}개", clazz.getName(), declaredMethods.length);

            for (Method declaredMethod : declaredMethods) {
                log.info("메서드 확인: 메서드={}, @RequestMapping 존재 여부={}", declaredMethod,
                        declaredMethod.isAnnotationPresent(RequestMapping.class));

                if (declaredMethod.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping annotation = declaredMethod.getAnnotation(RequestMapping.class);
                    String value = annotation.value();
                    RequestMethod[] method = annotation.method();

                    if (method.length == 0) {
                        method = RequestMethod.values();
                    }

                    for (RequestMethod requestMethod : method) {
                        HandlerKey handlerKey = new HandlerKey(value, requestMethod);
                        HandlerExecution execution = new HandlerExecution(controller, declaredMethod);
                        handlerExecutions.put(handlerKey, execution);
                        log.info("핸들러 등록: {} {} -> {}#{}", requestMethod, value,
                                clazz.getName(), declaredMethod.getName());
                    }
                }
            }
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        String requestURI = request.getRequestURI();
        HandlerKey key = new HandlerKey(requestURI, requestMethod);
        return handlerExecutions.get(key);
    }
}
