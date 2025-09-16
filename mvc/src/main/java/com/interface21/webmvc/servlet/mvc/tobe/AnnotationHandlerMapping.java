package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage; // 컨트롤러가 위치한 패키지 이름
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!"); // 핸들러 싹 다 가져오기
        try {
            var reflections = new Reflections(basePackage);
            var controllers = reflections.getTypesAnnotatedWith(Controller.class);
            for (Class<?> controller : controllers) { // @Controller 붙어있는 클래스
                for (Method method : controller.getMethods()) { // public 메서드
                    if (method.isAnnotationPresent(RequestMapping.class)) { // @RequestMapping 붙어있는 메서드
                        var annotation = method.getAnnotation(RequestMapping.class);
                        var value = annotation.value();
                        var httpMethods = annotation.method();
                        // HandlerKey 생성
                        // method 없으면 모든 메서드 지원
                        if (httpMethods.length == 0) {
                            for (RequestMethod requestMethod : RequestMethod.values()) {
                                var handlerKey = new HandlerKey(value, requestMethod);
                                Constructor<?> constructor = controller.getDeclaredConstructor();
                                handlerExecutions.put(handlerKey,
                                        new HandlerExecution(constructor.newInstance(), method));
                            }
                        } else {
                            for (RequestMethod httpMethod : httpMethods) {
                                var handlerKey = new HandlerKey(value, httpMethod);
                                Constructor<?> constructor = controller.getDeclaredConstructor();
                                handlerExecutions.put(handlerKey,
                                        new HandlerExecution(constructor.newInstance(), method));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {

        }
    }

    public Object getHandler(final HttpServletRequest request) { // 여기서 원하는 컨트롤러 찾아주기
        var requestURI = request.getRequestURI();
        var method = request.getMethod();
        var handlerKey = new HandlerKey(requestURI, RequestMethod.valueOf(method));
        return handlerExecutions.get(handlerKey);
    }
}
