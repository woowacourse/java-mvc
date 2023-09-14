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

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> classesWithController = reflections.getTypesAnnotatedWith(Controller.class);
        try {
            initializeHandlerExecutions(classesWithController);
        } catch (Exception e) {
            log.error("ERROR : {}", e.getMessage());
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void initializeHandlerExecutions(Set<Class<?>> classesWithController)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        for (Class<?> classWithController : classesWithController) {
            List<Method> methods = extractMethods(classWithController);

            for (Method method : methods) {
                RequestMapping requestMappingAnnotation = method.getAnnotation(RequestMapping.class);
                String url = requestMappingAnnotation.value();
                RequestMethod[] requestMethods = requestMappingAnnotation.method();

                for (RequestMethod requestMethod : requestMethods) {
                    HandlerKey handlerKey = new HandlerKey(url, requestMethod);
                    HandlerExecution handlerExecution =
                            new HandlerExecution(method, classWithController.getDeclaredConstructor().newInstance());

                    handlerExecutions.put(handlerKey, handlerExecution);
                }
            }
        }
    }

    private List<Method> extractMethods(Class<?> classWithController) {
        return Arrays.stream(classWithController.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toList());
    }

    public Object getHandler(final HttpServletRequest request) {
        String uri = request.getRequestURI();
        RequestMethod method = RequestMethod.valueOf(request.getMethod());

        return handlerExecutions.get(new HandlerKey(uri, method));
    }
}
