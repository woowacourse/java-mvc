package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.mvc.tobe.exception.ControllerClassNotFoundByNameException;
import webmvc.org.springframework.web.servlet.mvc.tobe.exception.ReflectionInstantiationException;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        for (final Object packageName : basePackage) {
            addHandlerExecutionsScannedInPackage(packageName);
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void addHandlerExecutionsScannedInPackage(final Object packageName) {
        final List<Class<?>> controllerClasses = getControllerClasses(packageName);

        for (Class<?> controllerClass : controllerClasses) {
            addHandlerExecutionsInController(controllerClass);
        }
    }

    private List<Class<?>> getControllerClasses(final Object name) {
        final Reflections reflections = new Reflections(name);
        final Set<String> controllerNames =
            reflections.get(Scanners.TypesAnnotated.with(Controller.class));

        final List<Class<?>> controllerClasses = new ArrayList<>();
        for (String controllerName : controllerNames) {
            try {
                controllerClasses.add(Class.forName(controllerName));
            } catch (ClassNotFoundException e) {
                throw new ControllerClassNotFoundByNameException(controllerName);
            }
        }

        return controllerClasses;
    }

    private void addHandlerExecutionsInController(final Class<?> controllerClass) {
        final List<Method> requestMappingMethods = getRequestMappingMethods(controllerClass);
        try {
            final Object controller = controllerClass.getDeclaredConstructor()
                .newInstance();

            for (Method requestMappingMethod : requestMappingMethods) {
                addHandlerExecutions(controller, requestMappingMethod);
            }
        } catch (InstantiationException | NoSuchMethodException
                 | IllegalAccessException | InvocationTargetException e) {
            throw new ReflectionInstantiationException("controller 인스턴스 생성에 실패하였습니다.");
        }
    }

    private List<Method> getRequestMappingMethods(final Class<?> controllerClass) {
        final List<Method> declaredMethods = List.of(controllerClass.getDeclaredMethods());
        return declaredMethods.stream()
            .filter(this::containsRequestMappingAnnotation)
            .collect(Collectors.toList());
    }

    private boolean containsRequestMappingAnnotation(final Method method) {
        final List<Class<? extends Annotation>> annotationTypes = Arrays.stream(
                method.getAnnotations())
            .map(Annotation::annotationType)
            .collect(Collectors.toList());

        return annotationTypes.contains(RequestMapping.class);
    }

    private void addHandlerExecutions(final Object controller, final Method method) {
        final RequestMapping annotation = method.getDeclaredAnnotation(RequestMapping.class);
        final String value = annotation.value();
        final RequestMethod[] requestMethods = annotation.method();

        for (RequestMethod requestMethod : requestMethods) {
            final HandlerKey handlerKey = new HandlerKey(value, requestMethod);
            final HandlerExecution handlerExecution = new HandlerExecution(controller, method);

            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final HandlerKey handlerKey = new HandlerKey(
            request.getRequestURI(),
            RequestMethod.valueOf(request.getMethod())
        );

        return handlerExecutions.get(handlerKey);
    }
}
