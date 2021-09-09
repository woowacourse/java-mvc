package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.mvc.HandlerMapping;
import nextstep.mvc.controller.exception.HandleKeyDuplicationException;
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

    public void initialize() {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);

        for (Class<?> aClass : classes) {
            try {
                Object instance = aClass.getDeclaredConstructor().newInstance();
                List<Method> declaredMethods = getRequestMappingMethods(aClass);

                addHandleExecution(instance, declaredMethods);
            } catch (Exception e) {
                log.error("initializeError : {}", e.getMessage());
            }
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private List<Method> getRequestMappingMethods(Class<?> aClass) {
        Method[] methods = aClass.getDeclaredMethods();
        return Arrays.stream(methods)
            .filter(method -> method.isAnnotationPresent(RequestMapping.class))
            .collect(Collectors.toList());
    }

    private void addHandleExecution(Object instance, List<Method> methods) {
        for (Method aMethod : methods) {
            RequestMapping requestMapping = aMethod.getAnnotation(RequestMapping.class);
            String url = requestMapping.value();
            for (RequestMethod requestMethod : requestMapping.method()) {
                HandlerKey handlerKey = new HandlerKey(url, requestMethod);
                validateHandleKeyDuplicate(handlerKey);
                handlerExecutions.put(handlerKey, new HandlerExecution(instance, aMethod));
                log.info("Request Mapping Uri : {}", url);
            }
        }
    }

    private void validateHandleKeyDuplicate(HandlerKey handlerKey) {
        if (handlerExecutions.containsKey(handlerKey)) {
            throw new HandleKeyDuplicationException();
        }
    }

    public Object getHandler(HttpServletRequest request) {
        return handlerExecutions.get(HandlerKey.of(request));
    }
}
