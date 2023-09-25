package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
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
import web.org.springframework.web.bind.annotation.MethodMapping;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.mvc.exception.AbstractClassConstructorException;
import webmvc.org.springframework.web.servlet.mvc.exception.DefaultConstructorAccessException;
import webmvc.org.springframework.web.servlet.mvc.exception.DefaultConstructorException;
import webmvc.org.springframework.web.servlet.mvc.exception.HandlerNotFoundException;

public class AnnotationHandlerMapping implements HandlerMapping {
    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        initHandlerExecutions();
        System.out.println(handlerExecutions);
        log.info("Initialized AnnotationHandlerMapping successfully!");
    }

    private void initHandlerExecutions() {
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        controllers.stream()
                .map(Class::getMethods)
                .flatMap(Arrays::stream)
                .filter(method -> MethodMapping.isAnyMatchAnnotation(method.getDeclaredAnnotations()))
                .forEach(this::addHandlerExecutions);
    }

    private void addHandlerExecutions(final Method method) {
        Arrays.stream(method.getDeclaredAnnotations())
                .map(this::parseHandlerKey)
                .forEach(handlerKey -> this.handlerExecutions.put(handlerKey, parseHandlerExecution(method)));
    }

    private HandlerKey parseHandlerKey(final Annotation annotation) {
        final String requestValue = extractValue(annotation);
        final RequestMethod requestMethod = extractMethod(annotation);
        return new HandlerKey(requestValue, requestMethod);
    }

    private String extractValue(final Annotation annotation) {
        try {
            return (String) annotation.annotationType()
                    .getDeclaredMethod("value")
                    .invoke(annotation);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new AnnotationMethodInvokeException("어노테이션의 메소드를 실행시키는 도중 예외가 발생했습니다.", e);
        }
    }

    private RequestMethod extractMethod(final Annotation annotation) {
        final RequestMapping requestMapping = annotation.annotationType()
                .getDeclaredAnnotation(RequestMapping.class);
        return requestMapping.method()[0];
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

    @Override
    public boolean isMatch(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final String method = request.getMethod();
        final RequestMethod requestMethod = RequestMethod.valueOf(method);
        final HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);

        return this.handlerExecutions.containsKey(handlerKey);
    }

    @Override
    public HandlerExecution getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = createHandlerKey(request);
        return Optional.ofNullable(handlerExecutions.get(handlerKey))
                .orElseThrow(() -> new HandlerNotFoundException("Handler를 찾을 수 없습니다. inputHandlerKey: " + handlerKey));
    }

    private HandlerKey createHandlerKey(final HttpServletRequest request) {
        final String method = request.getMethod();
        final RequestMethod requestMethod = RequestMethod.valueOf(method);
        return new HandlerKey(request.getRequestURI(), requestMethod);
    }
}
