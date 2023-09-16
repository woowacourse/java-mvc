package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
        final Set<Class<?>> classWithController = reflections.getTypesAnnotatedWith(Controller.class);
        for (final Class<?> clazz : classWithController) {
            final Object classInstance = createInstanceByClass(clazz);
            final List<Method> methodWithRequestMapping = Arrays.stream(clazz.getDeclaredMethods())
                    .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                    .collect(Collectors.toList());
            initializeHandlerExecutions(classInstance, methodWithRequestMapping);
        }
    }

    private void initializeHandlerExecutions(final Object classInstance, final List<Method> methodWithRequestMapping) {
        for (final Method method : methodWithRequestMapping) {
            final HandlerExecution handlerExecution = new HandlerExecution(classInstance, method);
            final RequestMapping annotation = method.getAnnotation(RequestMapping.class);
            final String url = annotation.value();
            final RequestMethod[] requestMethods = annotation.method();
            final List<HandlerKey> handlerKeys = Arrays.stream(requestMethods)
                    .map(requestMethod -> new HandlerKey(url, requestMethod))
                    .collect(Collectors.toList());
            for (final HandlerKey handlerKey : handlerKeys) {
                handlerExecutions.put(handlerKey, handlerExecution);
            }
        }
    }

    private Object createInstanceByClass(final Class<?> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (InstantiationException e) {
            log.error("{} 클래스는 추상클래스이거나 인터페이스입니다.", clazz.getSimpleName());
        } catch (IllegalAccessException e) {
            log.error("{} 클래스의 생성자에 접근할 수 없습니다.", clazz.getSimpleName());
        } catch (InvocationTargetException e) {
            log.error("{} 클래스를 생성할 때 예외가 발생하였습니다.", clazz.getSimpleName());
            log.error("TargetException: {}", e.getTargetException().getMessage());
        } catch (NoSuchMethodException e) {
            log.error("{} 클래스의 기본 생성자를 찾을 수 없습니다.", clazz.getSimpleName());
        }
        throw new IllegalArgumentException();
    }

    public Object getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        final HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);
        return handlerExecutions.get(handlerKey);
    }
}
