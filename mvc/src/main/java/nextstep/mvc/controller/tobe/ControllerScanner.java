package nextstep.mvc.controller.tobe;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerScanner {

    private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);

    public Map<HandlerKey, HandlerExecution> scannedControllerMethod(Object onePackage) {
        final Map<HandlerKey, HandlerExecution> handlerExecutions = new HashMap<>();
        final Reflections reflections = new Reflections((String) onePackage);
        final Set<Class<?>> allClasses = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> singleClass : allClasses) {
            processWithController(handlerExecutions, singleClass);
        }
        return handlerExecutions;
    }

    private void processWithController(Map<HandlerKey, HandlerExecution> handlerExecutions,
        Class<?> singleClass) {
        try {
            final RequestMapping controllerAnnotation = singleClass.getDeclaredAnnotation(
                RequestMapping.class);
            final Constructor<?> constructor = singleClass.getConstructor();
            final Object instance = constructor.newInstance();
            String controllerURLValue = controllerMapValue(controllerAnnotation);

            final Method[] methods = singleClass.getMethods();
            for (Method method : methods) {
                processWithMethodOnController(handlerExecutions, instance, controllerURLValue,
                    method);
            }
        } catch (Exception e) {
            log.error("스캐닝 도중 오류가 생겼습니다.");
        }
    }

    private void processWithMethodOnController(Map<HandlerKey, HandlerExecution> handlerExecutions,
        Object instance,
        String controllerURLValue, Method method) {
        final Annotation[] annotations = method.getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            processWithAnnotation(handlerExecutions, instance, controllerURLValue, method,
                annotation);
        }
    }

    private void processWithAnnotation(Map<HandlerKey, HandlerExecution> handlerExecutions,
        Object instance,
        String controllerURLValue, Method method, Annotation annotation) {
        if (annotation instanceof RequestMapping) {
            final RequestMapping requestMapping = (RequestMapping) annotation;
            final String subURLValue = requestMapping.value();
            final RequestMethod[] subMethods = requestMapping.method();
            insertHandlerExecutionOnMap(handlerExecutions, instance, controllerURLValue, method,
                subURLValue, subMethods);
        }
    }

    private void insertHandlerExecutionOnMap(Map<HandlerKey, HandlerExecution> handlerExecutions,
        Object instance,
        String controllerURLValue, Method method, String subURLValue, RequestMethod[] subMethods) {
        for (RequestMethod requestMethod : subMethods) {
            final HandlerKey handlerKey = new HandlerKey(
                controllerURLValue + subURLValue, requestMethod);
            final HandlerExecution handlerExecution = new HandlerExecution(instance, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private String controllerMapValue(RequestMapping controllerAnnotation) {
        if (controllerAnnotation != null) {
            return controllerAnnotation.value();
        } else {
            return "";
        }
    }
}
