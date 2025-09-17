package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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

    // 여기에서 초기화하면서 handlerExceptions 채우기
    public void initialize() {
        // @Controller 가 달린 클래스 찾기
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> annotatedControllers = findAnnotatedControllers(reflections);

        // @RequestMapping 이 달린 메서드 찾기
        for (Class<?> annotatedController : annotatedControllers) {
            List<Method> annotatedMethods = Arrays.stream(annotatedController.getDeclaredMethods())
                    .filter(m -> m.isAnnotationPresent(RequestMapping.class))
                    .toList();

            annotatedMethods.forEach(m -> {
                RequestMapping requestMapping = m.getAnnotation(RequestMapping.class);
                RequestMethod[] methods = requestMapping.method();
                for (RequestMethod method : methods) {
                    handlerExecutions.put(new HandlerKey(requestMapping.value(), method),
                            new HandlerExecution(annotatedController, m));
                }
            });
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private Set<Class<?>> findAnnotatedControllers(Reflections reflections) {
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    public Object getHandler(final HttpServletRequest request) {
        // 여기서 HandlerExecution 이 나가야함.
        // 하는 것 = request를 받아서 handlerExecution 찾아서 반환.
        // key는 handler key
        RequestMethod requestMethod = RequestMethod.getRequestMethodBy(request.getMethod());
        HandlerKey key = new HandlerKey(request.getRequestURI(), requestMethod);

        return handlerExecutions.get(key);
    }
}
