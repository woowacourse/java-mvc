package webmvc.org.springframework.web.servlet.mvc.tobe;

import static java.util.stream.Collectors.toUnmodifiableMap;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.mvc.exception.AbstractClassConstructorException;
import webmvc.org.springframework.web.servlet.mvc.exception.DefaultConstructorAccessException;
import webmvc.org.springframework.web.servlet.mvc.exception.DefaultConstructorException;
import webmvc.org.springframework.web.servlet.mvc.exception.HandlerNotFoundException;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        this.handlerExecutions.putAll(initHandlerExecutions(controllers));
        log.info("Initialized AnnotationHandlerMapping successfully!");
    }

    private Map<HandlerKey, HandlerExecution> initHandlerExecutions(final Set<Class<?>> controllers) {
        return controllers.stream()
                .map(Class::getMethods)
                .flatMap(Arrays::stream)
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(toUnmodifiableMap(
                        this::parseHandlerKey,
                        this::parseHandlerExecution,
                        (oldHandler, newHandler) -> oldHandler)
                );
    }

    private HandlerExecution parseHandlerExecution(final Method method) {
        final Class<?> controller = method.getDeclaringClass();
        final String controllerSimpleName = controller.getSimpleName();
        try {
            final Object controllerInstance = controller.getConstructor().newInstance();
            return new HandlerExecution(method, controllerInstance);
        } catch (NoSuchMethodException | InvocationTargetException e) {
            log.error("{} 클래스의 기본 생성자를 찾을 수 없습니다.", controllerSimpleName);
            final String messageFormat = String.format("%s 클래스의 기본 생성자를 찾을 수 없습니다.", controllerSimpleName);
            throw new DefaultConstructorException(messageFormat);
        } catch (InstantiationException e) {
            log.error("{} 클래스는 추상 클래스이기에 생성자를 가져올 수 없습니다.", controllerSimpleName);
            final String messageFormat = String.format("%s 클래스는 추상 클래스이기에 생성자를 가져올 수 없습니다.", controllerSimpleName);
            throw new AbstractClassConstructorException(messageFormat);
        } catch (IllegalAccessException e) {
            log.error("{} 클래스의 기본 생성자에 접근할 수 없습니다.", controllerSimpleName);
            final String messageFormat = String.format("%s 클래스의 기본 생성자에 접근할 수 없습니다.", controllerSimpleName);
            throw new DefaultConstructorAccessException(messageFormat);
        }
    }

    private HandlerKey parseHandlerKey(final Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final String requestValue = requestMapping.value();
        final RequestMethod requestMethod = requestMapping.method()[0];
        return new HandlerKey(requestValue, requestMethod);
    }

    public Object getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = createHandlerKey(request);
        return Optional.ofNullable(handlerExecutions.get(handlerKey))
                .orElseThrow(() -> new HandlerNotFoundException("Handler를 찾을 수 없습니다. inputHandlerKey: " + handlerKey));
    }

    private static HandlerKey createHandlerKey(final HttpServletRequest request) {
        final String method = request.getMethod();
        final RequestMethod requestMethod = RequestMethod.valueOf(method);
        return new HandlerKey(request.getRequestURI(), requestMethod);
    }
}
