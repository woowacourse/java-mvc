package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        // 지정된 패키지에서 @Controller가 붙은 클래스들을 찾는다.
        final var reflections = new Reflections(basePackage);
        final var controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);

        for (final var controllerClass : controllerClasses) {
            try {
                final var controller = controllerClass.getDeclaredConstructor().newInstance();

                // 컨트롤러 클래스의 메서드들을 확인한다.
                for (final var method : controllerClass.getDeclaredMethods()) {
                    final var requestMapping = method.getAnnotation(RequestMapping.class);

                    // @RequestMapping이 없으면 건너뛴다.
                    if (requestMapping == null) {
                        continue;
                    }

                    final String url = requestMapping.value();
                    final RequestMethod[] requestMethods = requestMapping.method();
                    final var execution = new HandlerExecution(controller, method);

                    // null은 HTTP 메서드 제한이 없다는 의미.
                    if (requestMethods.length == 0) {
                        register(new HandlerKey(url, null), execution);
                        continue;
                    }

                    for (final var requestMethod : requestMethods) {
                        register( new HandlerKey(url, requestMethod), execution);
                    }
                }
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException(
                        "컨트롤러 생성 실패: " + controllerClass.getName(),
                        e
                );
            }
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(final HttpServletRequest request) {
        final String url = request.getRequestURI();
        final RequestMethod requestMethod =
                RequestMethod.valueOf(request.getMethod());

        // 명시적인 HTTP 메서드 매핑을 우선한다.
        final var execution = handlerExecutions.get(new HandlerKey(url, requestMethod));

        if (execution != null) {
            return execution;
        }

        // 명시적인 매핑이 없으면 메서드를 생략한 매핑을 사용한다.
        return handlerExecutions.get(new HandlerKey(url, null));
    }


    private void register(
            final HandlerKey key,
            final HandlerExecution execution
    ) {
        if (handlerExecutions.putIfAbsent(key, execution) != null) {
            throw new IllegalStateException("중복 매핑: " + key);
        }
    }
}
