package nextstep.mvc.controller.tobe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Reflections reflections = new Reflections(basePackage);

        Set<Class<?>> classesAnnotatedWithController = reflections.getTypesAnnotatedWith(Controller.class);

        fillOutHandlerExecutions(classesAnnotatedWithController);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void fillOutHandlerExecutions(Set<Class<?>> classesAnnotatedWithController) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        for (Class<?> controllerClass : classesAnnotatedWithController) {
            List<Method> methodsWithRequestMapping = getMethodsAnnotatedWithRequestMapping(controllerClass);

            for (Method method : methodsWithRequestMapping) {
                RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                createHandler(controllerClass, method, annotation);
            }
        }
    }

    private List<Method> getMethodsAnnotatedWithRequestMapping(Class<?> controllerClass) {
        Method[] declaredMethods = controllerClass.getDeclaredMethods();
        return Arrays.stream(declaredMethods)
                     .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                     .collect(Collectors.toList());
    }

    private void createHandler(Class<?> controllerClass, Method method, RequestMapping annotation) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        String requestUri = annotation.value();
        RequestMethod[] requestMethods = annotation.method();

        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(requestUri, requestMethod);
            Object handler = controllerClass.getConstructor().newInstance();
            handlerExecutions.put(handlerKey, new HandlerExecution(handler, method));
        }
    }

    public Object getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        log.debug("Request Mapping Uri : {} , Request Method : {}", requestUri, requestMethod);

        HandlerKey handlerKey = new HandlerKey(requestUri, requestMethod);
        return handlerExecutions.get(handlerKey);
    }
}
