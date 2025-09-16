package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

    public void initialize()
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        log.info("Initialized AnnotationHandlerMapping!");
        // TODO 1: Reflections 라이브러리를 활용해 basePackage 위치에서 @Controller 어노테이션이 붙은 클래스 스캔
        String path = basePackage[0].toString();
        Reflections reflections = new Reflections(path);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        // TODO 2: basePackage 위치에서 @RequestMapping 이 붙은 핸들러 메서드 스캔
        for (Class<?> controller : controllers) {
            Object controllerInstance = controller.getDeclaredConstructor().newInstance(); // 여기서 한 번만
            log.debug("Controller: " + controller.getName());
            Method[] methods = controller.getDeclaredMethods();
            for (Method method : methods) {
                // TODO 3: 핸들러 메서드와 URL, HTTP Method 맵핑 정보 생성
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping mapping = method.getAnnotation(RequestMapping.class);
                    assert mapping != null;
                    String value = mapping.value();
                    RequestMethod[] requestMethods = mapping.method();

                    for (RequestMethod requestMethod : requestMethods) {
                        // TODO 4: HandlerKey 는 URL, HTTP Method 정보를 포함
                        HandlerKey handlerKey = new HandlerKey(value, requestMethod);
                        log.debug("HandlerKey : " + handlerKey);
                        // TODO 5: HandlerKey, HandlerExecution 생성 후 handlerExecutions 에 등록
                        HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, method);
                        handlerExecutions.put(handlerKey, handlerExecution);
                    }
                }
            }
        }


    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        log.debug("Target HandlerKey : " + handlerKey);
        return handlerExecutions.get(handlerKey);
    }
}
