package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

@Slf4j
@RequiredArgsConstructor
public class ControllerScanner {

    private final Object[] basePackages;

    public Map<HandlerKey, HandlerExecution> process() {
        final Reflections reflections = new Reflections(basePackages);

        final Map<HandlerKey, HandlerExecution> result = new HashMap<>();

        final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        for (final Class<?> controller : controllers) {
            validateModifier(controller);
            final Object controllerInstance = createControllerInstance(controller);

            for (final Method method : controller.getDeclaredMethods()) {
                validateModifier(method);
                final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                final RequestMethod[] supportedHttpMethods = getRequestMethods(requestMapping);
                final String requestPath = requestMapping.value();

                for (final RequestMethod httpMethod : supportedHttpMethods) {
                    final HandlerKey key = new HandlerKey(requestPath, httpMethod);
                    final HandlerExecution execution = HandlerExecution.of(controllerInstance, method);

                    if (result.containsKey(key)) {
                        throw new IllegalStateException("중복된 핸들러 매핑 감지, Key: %s".formatted(key));
                    }

                    result.put(key, execution);
                }
            }
        }

        return result;
    }

    private RequestMethod[] getRequestMethods(final RequestMapping requestMapping) {
        if (requestMapping.method().length == 0) {
            return RequestMethod.values();
        }
        return requestMapping.method();
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
}
