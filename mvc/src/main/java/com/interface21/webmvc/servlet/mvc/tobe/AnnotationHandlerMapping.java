package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
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

    // URL이 등록되지 않았으면 다른 HandlerMapping에게 기회를 주기 위해 null을 반환하고,
    // URL은 등록됐지만 HTTP 메서드가 맞지 않으면 404가 아닌 405로 응답할 수 있도록 예외를 던진다
    @Override
    public Object getHandler(final HttpServletRequest request) {
        final String url = request.getRequestURI();
        final String method = request.getMethod();

        final Set<RequestMethod> allowedMethods = findAllowedMethods(url);
        if (allowedMethods.isEmpty()) {
            return null;
        }

        return findRequestMethod(method)
                .filter(allowedMethods::contains)
                .map(requestMethod -> handlerExecutions.get(new HandlerKey(url, requestMethod)))
                .orElseThrow(() -> new MethodNotAllowedException(method, url, allowedMethods));
    }

    private Set<RequestMethod> findAllowedMethods(final String url) {
        return Arrays.stream(RequestMethod.values())
                .filter(requestMethod -> handlerExecutions.containsKey(new HandlerKey(url, requestMethod)))
                .collect(Collectors.toUnmodifiableSet());
    }

    // RequestMethod에 정의되지 않은 메서드(ex. CONNECT)가 들어와도 500이 아닌 405로 응답하기 위해 Optional로 감싼다
    private Optional<RequestMethod> findRequestMethod(final String method) {
        return Arrays.stream(RequestMethod.values())
                .filter(requestMethod -> requestMethod.name().equals(method))
                .findAny();
    }
}
