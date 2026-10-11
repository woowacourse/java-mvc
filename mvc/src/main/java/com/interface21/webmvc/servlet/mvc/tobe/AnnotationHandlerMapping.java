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
        // 1. 지정된 패키지에서 @Controller가 붙은 클래스들을 찾는다.
        final var reflections = new Reflections(basePackage);
        final var controllerClasses =
                reflections.getTypesAnnotatedWith(Controller.class);

        for (final var controllerClass : controllerClasses) {
            try {
                // 2. 컨트롤러 객체를 생성한다.
                final var controller = controllerClass.getDeclaredConstructor().newInstance();

                // 3. 컨트롤러 클래스의 메서드들을 확인한다.
                for (final var method : controllerClass.getDeclaredMethods()) {
                    final var requestMapping = method.getAnnotation(RequestMapping.class);

                    // @RequestMapping이 없으면 건너뛴다.
                    if (requestMapping == null) {
                        continue;
                    }

                    // 4. 어노테이션에서 URL과 HTTP 메서드를 읽는다.
                    final String url = requestMapping.value();
                    RequestMethod[] requestMethods = requestMapping.method();

                    // HTTP 메서드를 생략했으면 모든 메서드를 지원한다.
                    if (requestMethods.length == 0) {
                        requestMethods = RequestMethod.values();
                    }

                    // 5. 실행할 컨트롤러 객체와 메서드를 묶는다.
                    final var execution = new HandlerExecution(controller, method);

                    // 6. (URL, HTTP 메서드)를 키로 Map에 등록한다.
                    for (final var requestMethod : requestMethods) {
                        final var key = new HandlerKey(url, requestMethod);
                        handlerExecutions.put(key, execution);
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
        final var key = new HandlerKey(
                request.getRequestURI(),
                RequestMethod.valueOf(request.getMethod())
        );

        return handlerExecutions.get(key);
    }
}
