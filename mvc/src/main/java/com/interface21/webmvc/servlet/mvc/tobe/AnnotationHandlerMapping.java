package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;

    // HandlerKey를 통해 url과 HTTP 메서드를 매핑해서 보여준다
    // HandlerKey를 Map의 Key로 사용하려면 equals, hashcode를 제대로 오버라이딩해놔야한다
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        // basePackage 하위의 @Controller 클래스를 찾아 인스턴스로 만드는 일은 ControllerScanner에게 맡긴다
        final ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        final Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        // 컨트롤러 클래스와 그 인스턴스를 함께 넘겨 핸들러를 등록한다
        for (Map.Entry<Class<?>, Object> entry : controllers.entrySet()) {
            registerRequestMappingHandler(entry.getKey(), entry.getValue());
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void registerRequestMappingHandler(final Class<?> controllerClass, final Object controller) {
        // 컨트롤러에 직접 선언된 메서드 중 @RequestMapping이 붙은 메서드만 고른다
        final List<Method> handlerMethods = Arrays.stream(controllerClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .toList();

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

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final String url = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());

        return handlerExecutions.get(new HandlerKey(url, requestMethod));
    }
}
