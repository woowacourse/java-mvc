package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        makeHandlerExecutions(basePackages);
    }

    private void makeHandlerExecutions(final Object... basePackages) {
        for (final Object basePackage : basePackages) {
            final Reflections reflections = new Reflections(basePackage);
            final var classes = reflections.getTypesAnnotatedWith(Controller.class);
            putHandlerExecutionsByControllers(classes);
        }
    }

    private void putHandlerExecutionsByControllers(final Set<Class<?>> classes) {
        for (final Class<?> clazz : classes) {
            final Object instance = getNewInstance(clazz);
            final List<Method> methods = getMethodsWithAnnotation(clazz);
            putHandlerExecutionsByMethods(instance, methods);
        }
    }

    private Object getNewInstance(final Class<?> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("클래스 객체를 인스턴스화 할 수 없습니다.");
        } catch (IllegalAccessException e) {
            throw new RuntimeException("클래스 객체의 생성자에 접근할 수 없습니다.");
        } catch (InvocationTargetException e) {
            throw new RuntimeException("클래스 객체의 생성자를 호출할 수 없습니다.");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("클래스 객체의 생성자를 찾을 수 없습니다.");
        }
    }

    private List<Method> getMethodsWithAnnotation(final Class<?> clazz) {
        return Arrays.stream(clazz.getMethods())
            .filter(method -> method.isAnnotationPresent(RequestMapping.class))
            .collect(Collectors.toList());
    }

    private void putHandlerExecutionsByMethods(final Object object, final List<Method> methods) {
        for (final Method method : methods) {
            putHandlerExecutionsByMethod(object, method);
        }
    }

    private void putHandlerExecutionsByMethod(final Object object, final Method method) {
        final Map<HandlerKey, HandlerExecution> handlerExecutionsByMethod =
            convertHandlerExecutions(object, method);

        handlerExecutions.putAll(handlerExecutionsByMethod);
    }

    private Map<HandlerKey, HandlerExecution> convertHandlerExecutions(final Object object, final Method method) {
        final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        final String requestURI = requestMapping.value();
        final List<RequestMethod> requestMethods = List.of(requestMapping.method());

        return requestMethods.stream()
            .map(requestMethod -> new HandlerKey(requestURI, requestMethod))
            .collect(Collectors.toMap(
                handlerKey -> handlerKey,
                handlerKey -> new HandlerExecution(object, method)
            ));
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = makeHandlerKey(request);
        return handlerExecutions.get(handlerKey);
    }

    private HandlerKey makeHandlerKey(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final String method = request.getMethod();
        final RequestMethod requestMethod = RequestMethod.valueOf(method);
        return new HandlerKey(requestURI, requestMethod);
    }
}
