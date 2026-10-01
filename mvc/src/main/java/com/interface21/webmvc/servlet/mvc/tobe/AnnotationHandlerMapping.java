package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;

    // HandlerKey를 통해 url과 HTTP 메서드를 매핑해서 보여준다
    // HandlerKey를 Map의 Key로 사용하려면 equals, hashcode를 제대로 오버라이딩해놔야한다
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        // basePackage 하위에서 @Controller가 붙은 클래스를 모두 찾는다
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> controllerClass : controllerClasses) {
            registerController(controllerClass);
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void registerController(final Class<?> controllerClass) {
        final Object controller = createInstance(controllerClass);

        // 컨트롤러에 직접 선언된 메서드 중 @RequestMapping이 붙은 메서드만 등록한다
        for (Method method : controllerClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                registerHandler(controller, method);
            }
        }
    }

    private Object createInstance(final Class<?> controllerClass) {
        try {
            return controllerClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("컨트롤러를 생성할 수 없습니다: " + controllerClass.getName(), e);
        }
    }

    private void registerHandler(final Object controller, final Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final HandlerExecution handlerExecution = new HandlerExecution(controller, method);

        for (RequestMethod requestMethod : findRequestMethods(requestMapping)) {
            final HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
            handlerExecutions.put(handlerKey, handlerExecution);
            log.info("Mapped {} -> {}", handlerKey, method.getName());
        }
    }

    // method 설정이 없으면 모든 HTTP 메서드를 지원한다
    private RequestMethod[] findRequestMethods(final RequestMapping requestMapping) {
        final RequestMethod[] requestMethods = requestMapping.method();
        if (requestMethods.length == 0) {
            return RequestMethod.values();
        }
        return requestMethods;
    }

    public Object getHandler(final HttpServletRequest request) {
        final String url = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());

        return handlerExecutions.get(new HandlerKey(url, requestMethod));
    }
}
