package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping{

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");

        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> typesAnnotatedWithController = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> controller : typesAnnotatedWithController) {
            mapHandlerForAnnotatedController(controller);
        }
    }

    private void mapHandlerForAnnotatedController(Class<?> controller) {
        Method[] declaredMethods = controller.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            mapHandlerForAnnotatedMethod(declaredMethod);
        }
    }

    private void mapHandlerForAnnotatedMethod(Method declaredMethod) {
        if (declaredMethod.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping annotation = declaredMethod.getAnnotation(RequestMapping.class);
            RequestMethod[] method = annotation.method();
            mapHandlerForRequestMethods(declaredMethod, annotation, method);
        }
    }

    private void mapHandlerForRequestMethods(Method declaredMethod, RequestMapping annotation, RequestMethod[] method) {
        for (RequestMethod requestMethod : method) {
            HandlerKey handlerKey = new HandlerKey(annotation.value(), requestMethod);
            HandlerExecution handlerExecution = new HandlerExecution(declaredMethod);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        return handlerExecutions.get(new HandlerKey(requestURI, RequestMethod.valueOf(method)));
    }
}
