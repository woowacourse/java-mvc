package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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
        final Reflections reflections = new Reflections(basePackage);

        final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        for (final Class<?> controller : controllers) {
            validateModifier(controller);
            final Object controllerInstance = createControllerInstance(controller);
            for (final Method method : controller.getDeclaredMethods()) {
                validateModifier(method);
                final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                final RequestMethod[] supportedHttpMethods = requestMapping.method();
                final String requestPath = requestMapping.value();

                for (final RequestMethod httpMethod : supportedHttpMethods) {
                    final HandlerKey key = new HandlerKey(requestPath, httpMethod);
                    final HandlerExecution execution = HandlerExecution.of(controllerInstance, method);
                    handlerExecutions.put(key, execution);
                }
            }
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void validateModifier(final Class<?> controller) {
        if (!Modifier.isPublic(controller.getModifiers())) {
            throw new IllegalStateException("@Controller가 붙은 클래스는 public 이어야 합니다: " + controller.getName());
        }
    }

    private void validateModifier(final Method method) {
        if (!Modifier.isPublic(method.getModifiers())) {
            throw new IllegalStateException("@RequestMapping 메서드는 public 이어야 합니다: "
                    + method.getDeclaringClass().getName() + "#" + method.getName());
        }
    }

    private Object createControllerInstance(final Class<?> controller) {
        try {
            return controller.getDeclaredConstructor().newInstance();
        } catch (final Exception e) {
            throw new IllegalStateException("컨트롤러 인스턴스 생성 실패:" + controller.getName(), e);
        }
    }

    public HandlerExecution getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = new HandlerKey(
                request.getRequestURI(),
                RequestMethod.valueOf(request.getMethod()));

        final HandlerExecution handlerExecution = handlerExecutions.get(handlerKey);
        if (handlerExecution == null) {
            throw new IllegalStateException("No HandlerExecution found for key " + handlerKey);
        }
        return handlerExecution;
    }
}
