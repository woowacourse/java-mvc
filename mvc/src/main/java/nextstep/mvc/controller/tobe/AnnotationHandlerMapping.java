package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.servlet.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> controller : controllers) {
            mapMethods(controller);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void mapMethods(Class<?> controller) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Object newController = controller.getConstructor().newInstance();
        Method[] methods = controller.getMethods();
        for (Method method : methods) {
            Annotation[] annotations = method.getAnnotations();
            mapAnnotations(newController, method, annotations);
        }
    }

    private void mapAnnotations(Object newController, Method method, Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if(annotation.annotationType().equals(RequestMapping.class)) {
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                String url = requestMapping.value();
                RequestMethod[] requestMethods = requestMapping.method();
                mapHandlerExecutions(newController, method, url, requestMethods);
            }
        }
    }

    private void mapHandlerExecutions(Object newController, Method method, String url, RequestMethod[] requestMethods) {
        for(RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(url, requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(method, newController);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public Object getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod method = RequestMethod.valueOf(request.getMethod());

        HandlerKey handlerKey = new HandlerKey(requestURI, method);

        try {
            return handlerExecutions.get(handlerKey);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
