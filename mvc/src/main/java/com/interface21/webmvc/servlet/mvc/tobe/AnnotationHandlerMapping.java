package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Stream;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);
    private static final Class<? extends RequestMapping> MAPPING_ANNOTATION = RequestMapping.class;

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        Reflections reflections = new Reflections(basePackage);
        reflections.getTypesAnnotatedWith(Controller.class)
                .forEach(this::initializeControllerHandlers);
    }

    private void initializeControllerHandlers(Class<?> clazz) {
        try {
            Object instance = clazz.getDeclaredConstructor().newInstance();
            Stream.of(clazz.getDeclaredMethods())
                    .filter(method -> method.isAnnotationPresent(MAPPING_ANNOTATION))
                    .forEach(method -> initializeHandler(method, instance));
        } catch (Exception e) {
            log.error("메소드 핸들러 초기화 오류", e);
            throw new RuntimeException(clazz + "의 메소드 핸들러를 초기화할 수 없습니다.");
        }
    }

    private void initializeHandler(Method method, Object instance) {
        RequestMapping annotation = method.getAnnotation(MAPPING_ANNOTATION);
        RequestMethod[] requestMethods = annotation.method();
        String mappingUrl = annotation.value();

        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey key = new HandlerKey(mappingUrl, requestMethod);
            handlerExecutions.put(key, new HandlerExecution(method, instance));
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.from(request.getMethod()));
        if (!handlerExecutions.containsKey(handlerKey)) {
            throw new NoSuchElementException(handlerKey + "와 일치하는 핸들러 메소드가 없습니다");
        }
        return handlerExecutions.get(handlerKey);
    }
}
