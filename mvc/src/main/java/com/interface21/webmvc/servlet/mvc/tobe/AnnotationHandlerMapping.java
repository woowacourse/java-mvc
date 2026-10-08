package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
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

        // 컨트롤러 클래스를 순회
        for (Class<?> controllerClass : controllerClasses) {
            // 해당하는 컨트롤러를 등록함
            registerRequestMappingHandler(controllerClass);
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void registerRequestMappingHandler(final Class<?> controllerClass) {
        // 컨트롤러에 직접 선언된 메서드 중 @RequestMapping이 붙은 메서드만 고른다
        final List<Method> handlerMethods = Arrays.stream(controllerClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .toList();

        // 등록할 핸들러가 없으면 인스턴스를 만들지 않는다
        if (handlerMethods.isEmpty()) {
            return;
        }

        // 조건을 통과한 컨트롤러만 한 번 생성해서 모든 핸들러가 공유한다
        final Object controller;
        try {
            controller = controllerClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("컨트롤러를 생성할 수 없습니다: " + controllerClass.getName(), e);
        }

        for (Method method : handlerMethods) {
            final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            final HandlerExecution handlerExecution = new HandlerExecution(controller, method);

            // method 설정이 없으면 모든 HTTP 메서드를 지원한다
            RequestMethod[] requestMethods = requestMapping.method();
            if (requestMethods.length == 0) {
                requestMethods = RequestMethod.values();
            }

            for (RequestMethod requestMethod : requestMethods) {
                final HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
                handlerExecutions.put(handlerKey, handlerExecution);
                log.info("Mapped {} -> {}", handlerKey, method.getName());
            }
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final String url = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());

        return handlerExecutions.get(new HandlerKey(url, requestMethod));
    }
}
