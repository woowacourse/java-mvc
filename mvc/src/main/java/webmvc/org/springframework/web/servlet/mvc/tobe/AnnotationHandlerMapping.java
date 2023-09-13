package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.mvc.exception.ClassException;

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

        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

        controllers.stream().map(Class::getDeclaredMethods)
                .flatMap(Arrays::stream)
                .filter(this::isRequestMappingAnnotation)
                .forEach(this::addHandlerExecution);
    }

    private boolean isRequestMappingAnnotation(Method method) {
        return method.isAnnotationPresent(RequestMapping.class);
    }

    private void addHandlerExecution(Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        RequestMethod[] httpMethods = requestMapping.method();
        Object instance = getInstance(method.getDeclaringClass());

        for (RequestMethod httpMethod : httpMethods) {
            HandlerKey handlerKey = new HandlerKey(requestMapping.value(), httpMethod);
            HandlerExecution handlerExecution = new HandlerExecution(method, instance);

            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private Object getInstance(Class<?> clazz) {
        try {
            Constructor<?> declaredConstructor = clazz.getDeclaredConstructor();
            return declaredConstructor.newInstance();
        } catch (NoSuchMethodException e) {
            throw new ClassException(String.format("해당 클래스(%s)에 기본 생성자가 없습니다 ", clazz.getName()), e);
        } catch (InstantiationException e) {
            throw new ClassException(String.format("해당 클래스(%s)의 인스턴스를 생성할 수 없습니다 ", clazz.getName()), e);
        } catch (IllegalAccessException e) {
            throw new ClassException(String.format("해당 클래스(%s)의 생성자에 접근할 수 없습니다 ", clazz.getName()), e);
        } catch (InvocationTargetException e) {
            throw new ClassException(String.format("해당 클래스(%s)의 생성자 호출 중 예외가 발생했습니다 ", clazz.getName()), e);
        }
    }


    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String httpMethod = request.getMethod();
        HandlerKey handlerKey = new HandlerKey(requestURI, RequestMethod.valueOf(httpMethod));

        return handlerExecutions.get(handlerKey);
    }
}
