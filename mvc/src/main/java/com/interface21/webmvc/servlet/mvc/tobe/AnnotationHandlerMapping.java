package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
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
        final Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        controllerClasses.forEach(this::registerController);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(final HttpServletRequest request) {
        String path = request.getRequestURI();
        if(request.getContextPath() != null) {
            path = path.substring(request.getContextPath().length());
        }

        final HandlerKey requestHandlerKey = new HandlerKey(
                path,
                RequestMethod.valueOf(request.getMethod())
        );
        return handlerExecutions.get(requestHandlerKey);
    }

    private void registerController(Class<?> controllerClass) {
        final Object controller = createControllerInstance(controllerClass);

        Arrays.stream(controllerClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .forEach(method -> registerHandlerExecution(method, controller));
    }


    private Object createControllerInstance(Class<?> controllerClass) {
        try {
            return controllerClass.getConstructor().newInstance();
        } catch (ReflectiveOperationException ex) {
            handleReflectionException(ex, controllerClass);
            return null;
        }
    }

    private void handleReflectionException(ReflectiveOperationException ex, Class<?> controllerClass) {
        if(ex instanceof InstantiationException || ex instanceof InvocationTargetException){
            log.error("Controller 인스턴스 생성에 실패했습니다: {}", controllerClass.getName());
            throw new IllegalStateException("Controller 인스턴스 생성에 실패했습니다: " + controllerClass.getName(), ex);
        }
        if(ex instanceof IllegalAccessException) {
            log.error("메서드에 대한 접근 권한이 없습니다.");
            throw new IllegalStateException("메서드에 대한 접근 권한이 없습니다.", ex);
        }
        if(ex instanceof NoSuchMethodException) {
            log.error("기본 생성자를 찾을 수 없습니다: {}", controllerClass.getName());
            throw new IllegalStateException("기본 생성자를 찾을 수 없습니다: " + controllerClass.getName(), ex);
        }
        throw new IllegalStateException("알 수 없는 Reflection 예외가 발생했습니다.", ex);
    }

    private void registerHandlerExecution(Method handlerMethod, Object controller) {
        final RequestMapping requestMapping = handlerMethod.getDeclaredAnnotation(RequestMapping.class);
        final String path = requestMapping.value();

        Arrays.stream(requestMapping.method())
                .forEach(httpMethod -> registerSingleHandler(path, httpMethod, controller, handlerMethod));
    }

    private void registerSingleHandler(String path, RequestMethod httpMethod, Object controller, Method handlerMethod) {
        final HandlerKey handlerKey = new HandlerKey(path, httpMethod);
        final HandlerExecution handlerExecution = new HandlerExecution(controller, handlerMethod);

        if(handlerExecutions.containsKey(handlerKey)){
            throw new IllegalStateException("해당 경로에 대한 요청에 대해 중복되는 핸들러가 존재합니다: " + handlerKey);
        }

        handlerExecutions.put(handlerKey, handlerExecution);
    }
}
