package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("AnnotationHandlerMapping 초기화 시작 - basePackage: {}", Arrays.toString(basePackage));
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        for (final Class<?> controllerClass : controllerClasses) {
            registerHandlerExecution(controllerClass);
        }
        log.info("AnnotationHandlerMapping 초기화 완료 - 총 {}개의 핸들러 등록", handlerExecutions.size());
        if (log.isDebugEnabled()) {
            handlerExecutions.forEach((key, value) -> log.debug("등록된 핸들러 - {}", key));
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.of(request.getMethod());
        if (requestMethod == null) {
            log.warn("지원하지 않는 HTTP 메서드: {}", request.getMethod());
            return null;
        }
        final HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);
        log.debug("요청 URI: {}, Method: {}", requestURI, requestMethod);
        return handlerExecutions.get(handlerKey);
    }

    private void registerHandlerExecution(final Class<?> controllerClass) {
        final Object controllerInstance = getControllerInstance(controllerClass);
        final List<Method> targetMethods = getTargetMethods(controllerClass);
        targetMethods.forEach(method -> addHandlerExecutions(controllerInstance, method));
    }

    private Object getControllerInstance(final Class<?> controllerClass) {
        try {
            return controllerClass.getConstructor().newInstance();
        } catch (final NoSuchMethodException exception) {
            log.error("Controller '{}'에서 기본 생성자를 찾을 수 없습니다.", controllerClass.getName(), exception);
            throw new IllegalStateException("기본 생성자가 없는 컨트롤러는 인스턴스화할 수 없습니다.", exception);
        } catch (final InstantiationException | IllegalAccessException | InvocationTargetException exception) {
            log.error("Controller '{}' 인스턴스 생성에 실패했습니다. (추상 클래스, 접근 불가 생성자 등)", controllerClass.getName(), exception);
            throw new IllegalStateException("컨트롤러 인스턴스 생성에 실패했습니다. 클래스 설정을 확인해주세요.", exception);
        }
    }

    private List<Method> getTargetMethods(final Class<?> controllerClass) {
        return Stream.of(controllerClass.getMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .toList();
    }

    private void addHandlerExecutions(
            final Object controllerInstance,
            final Method method
    ) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final RequestMethod[] effectiveMethods = getRequestMethods(requestMapping);
        for (final RequestMethod requestMethod : effectiveMethods) {
            final HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
            final HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private RequestMethod[] getRequestMethods(final RequestMapping requestMapping) {
        final RequestMethod[] requestMethods = requestMapping.method();
        if (requestMethods.length == 0) {
            return RequestMethod.values();
        }
        return requestMethods;
    }
}
