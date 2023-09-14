package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Method;
import java.util.*;

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
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        for(Class<?> controllerClass : controllerClasses) {
            Method[] methods = controllerClass.getDeclaredMethods();
            for(Method method : methods) {
                addHandlerExecution(controllerClass, method);
            }
        }
    }

    private Object findController(final Class<?> classWithAnnotation) {
        try{
            return classWithAnnotation.getConstructor().newInstance();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new IllegalStateException("존재하지 않는 페이지 입니다.");
        }
    }

    private void addHandlerExecution(final Class<?> controllerClass, final Method method) {
        Object controller = findController(controllerClass);
        RequestMapping declaredAnnotation = method.getDeclaredAnnotation(RequestMapping.class);
        if(declaredAnnotation == null) {
            return;
        }
        String urlValue = declaredAnnotation.value();
        RequestMethod[] requestMethodsOfUrlValue = declaredAnnotation.method();
        List<HandlerKey> handlerKeyList = getHandlerKeyList(urlValue, requestMethodsOfUrlValue);
        for(HandlerKey handlerKey : handlerKeyList) {
            handlerExecutions.put(handlerKey, new HandlerExecution(controller, method));
        }
    }

    private List<HandlerKey> getHandlerKeyList(final String urlValue, final RequestMethod[] requestMethodsOfUrlValue) {
        List<HandlerKey> handlerKeyList = new ArrayList<>();
        for(RequestMethod requestMethod : requestMethodsOfUrlValue) {
            HandlerKey handlerKey = new HandlerKey(urlValue, requestMethod);
            handlerKeyList.add(handlerKey);
        }
        return handlerKeyList;
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);
        return handlerExecutions.get(handlerKey);
    }
}
