package com.interface21.webmvc.servlet.mvc.mapping;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.adapter.HandlerExecution;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    /**
     * basePackage 안의 Controller 클래스 모두 찾는다 각 Controller 클래스의 인스턴스를 생성한다 RequestMapping 어노테이션이 붙은 메서드만 골라낸다 (url,
     * requestMethod)키로 HandlerExecution를 등록한다
     */
    public void initialize() {
        Reflections reflections = new Reflections(basePackage);
        ControllerScanner controllerScanner = new ControllerScanner(reflections);
        Map<Class<?>, Object> controllers = controllerScanner.buildControllerRegistry();

        try {
            for (Entry<Class<?>, Object> entry : controllers.entrySet()) {
                Class<?> clazz = entry.getKey();
                Object instance = entry.getValue();
                // @RequestMapping 붙은 메서드만 골라냄 ex) findUserId(), save()
                Set<Method> methods = getRequestMappingMethods(clazz);
                // HandlerExecution 테이블에 등록
                methods.forEach(method -> putHandlerExecutionByRequestMapping(instance, method));
            }
        } catch (Exception e) {
            throw new IllegalStateException("HandlerMapping initialization failed", e);
        }

        if (log.isInfoEnabled()) {
            log.info("Initialized AnnotationHandlerMapping: {} handlers", handlerExecutions.size());
            handlerExecutions.keySet().forEach(key -> log.info("Handler mapped: {}", key));
        }
    }

    /**
     * HttpServletRequest를 통해 실행할 HandlerExecution을 찾는다 요청에서 uri, http method 추출해서 handlerKey 만들고 이 키로 handlerExecution
     * 조회
     */
    @Override
    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        System.out.println("context path: " + request.getContextPath());
        String contextPath = request.getContextPath();
        if (contextPath == null) {
            contextPath = "";
        }
        String path = requestURI.substring(contextPath.length());

        RequestMethod method = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(path, method);

        return handlerExecutions.get(handlerKey);
    }

    private Set<Method> getRequestMappingMethods(final Class<?> controllerClass) {
        return ReflectionUtils.getAllMethods(controllerClass, ReflectionUtils.withAnnotation(RequestMapping.class));
    }

    private void putHandlerExecutionByRequestMapping(
            final Object instance,
            final Method method
    ) {
        RequestMapping mapping = method.getAnnotation(RequestMapping.class);
        if (mapping == null) {
            return;
        }
        String url = mapping.value(); // ex "/get-test"
        RequestMethod[] requestMethods = mapping.method(); // ex GET, POST, ...

        if (requestMethods.length == 0) {
            requestMethods = RequestMethod.values();
        }

        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(url, requestMethod);
            HandlerExecution execution = new HandlerExecution(instance, method);
            handlerExecutions.put(handlerKey, execution);
        }
    }
}
