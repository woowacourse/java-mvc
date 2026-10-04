package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.core.util.ReflectionUtils;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
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

    public void initialize() {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> clazz : controllerClasses) {
            registerController(clazz);
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    // @Controller가 붙은 컨트롤러 클래스 하나에서, @RequestMapping 붙은 메서드를 처리할 Handler 등록
    private void registerController(final Class<?> clazz) {
        Object controller = createController(clazz);
        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                registerHandler(method, controller);
            }
        }
    }

    // @Controller가 붙은 컨트롤러 클래스 객체 생성
    private static Object createController(final Class<?> clazz) {
        try {
            return ReflectionUtils.accessibleConstructor(clazz).newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new IllegalStateException("[ERROR] 컨트롤러 생성에 실패했습니다. class: " + clazz.getName(), e);
        }
    }

    // @RequestMapping의 url과 method를 가지고, 어떤 요청(HandlerKey)을 어떤 HandlerExecution이 처리할지 HandlerExecution 등록
    private void registerHandler(final Method method, final Object controller) {
        validateReturnType(method);
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        HandlerExecution handlerExecution = new HandlerExecution(controller, method);
        for (RequestMethod requestMethod : getRequestMethods(requestMapping)) {
            HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
            validateDuplicatedHandlerKey(handlerKey);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private void validateDuplicatedHandlerKey(final HandlerKey handlerKey) {
        if (handlerExecutions.containsKey(handlerKey)) {
            throw new IllegalStateException("[ERROR] 이미 등록된 요청 매핑입니다. handlerKey: " + handlerKey);
        }
    }

    private static void validateReturnType(final Method method) {
        if (method.getReturnType() != ModelAndView.class) {
            throw new IllegalStateException(
                    "[ERROR] @RequestMapping 메서드는 ModelAndView를 반환해야 합니다. method: " + method.getDeclaringClass()
                            .getName() + "." + method.getName());
        }
    }

    // @RequestMapping이 달린 메서드들의 HTTP method 목록 반환 (설정되어 있지 않으면 모든 HTTP method)
    private static RequestMethod[] getRequestMethods(final RequestMapping requestMapping) {
        if (requestMapping.method().length == 0) {
            return RequestMethod.values();
        }
        return requestMapping.method();
    }

    public Object getHandler(final HttpServletRequest request) {
        return handlerExecutions.get(
                new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod())));
    }
}
