package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import webmvc.org.springframework.web.servlet.ModelAndView;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);
    private static final int REQUEST_INDEX = 0;
    private static final int RESPONSE_INDEX = 1;

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        Set<Class<?>> controllerClasses = findClassesAnnotatedController();

        initializeHandlerExecutions(controllerClasses);

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private Set<Class<?>> findClassesAnnotatedController() {
        Reflections reflections = new Reflections(basePackage);

        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private void initializeHandlerExecutions(Set<Class<?>> controllerClasses) {
        controllerClasses.forEach(controlerClass -> {
            List<Method> methods = extractMethods(controlerClass);

            initializeHandlerExecutionsByMethods(methods);
        });
    }

    private List<Method> extractMethods(Class<?> controllerClasses) {
        return Arrays.stream(controllerClasses.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .filter(method -> method.getReturnType().equals(ModelAndView.class))
                .filter(method -> validateParameterType(method.getParameterTypes()))
                .collect(Collectors.toList());
    }

    private boolean validateParameterType(Class<?>[] parameters) {
        return parameters[REQUEST_INDEX].equals(HttpServletRequest.class) &&
                parameters[RESPONSE_INDEX].equals(HttpServletResponse.class);
    }

    private void initializeHandlerExecutionsByMethods(List<Method> methods) {
        methods.forEach(this::setupHandlerExecutions);
    }

    private void setupHandlerExecutions(Method method) {
        RequestMapping requestMappingAnnotation = method.getAnnotation(RequestMapping.class);
        String url = requestMappingAnnotation.value();
        RequestMethod[] requestMethods = requestMappingAnnotation.method();

        try {
            for (RequestMethod requestMethod : requestMethods) {
                HandlerKey handlerKey = new HandlerKey(url, requestMethod);
                Object classInstance = extractClassInstance(method);
                HandlerExecution handlerExecution = new HandlerExecution(method, classInstance);

                handlerExecutions.put(handlerKey, handlerExecution);
            }
        } catch (Exception e) {
            log.error("ERROR : {}", e.getMessage());
        }
    }

    private Object extractClassInstance(Method method) throws Exception {
        Class<?> declaringClassForMethod = method.getDeclaringClass();

        return declaringClassForMethod.getDeclaredConstructor().newInstance();
    }

    public Object getHandler(final HttpServletRequest request) {
        String uri = request.getRequestURI();
        RequestMethod method = RequestMethod.valueOf(request.getMethod());

        return handlerExecutions.get(new HandlerKey(uri, method));
    }

}
