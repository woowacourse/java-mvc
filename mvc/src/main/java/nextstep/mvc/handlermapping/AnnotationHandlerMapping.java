package nextstep.mvc.handlermapping;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import nextstep.mvc.controller.tobe.ControllerScanner;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.controller.tobe.HandlerKey;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public Object getHandler(HttpServletRequest request) {
        return handlerExecutions.get(
            new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()))
        );
    }

    public List<HandlerKey> mapHandlerKeys(String url, RequestMethod[] requestMethods) {
        List<HandlerKey> handlerKeys = new ArrayList<>();

        for (RequestMethod requestMethod : requestMethods) {
            handlerKeys.add(new HandlerKey(url, requestMethod));
        }

        return handlerKeys;
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        for (Object obj : basePackage) {
            String packages = (String) obj;
            ControllerScanner controllerScanner = new ControllerScanner(packages);
            Map<Class<?>, Object> controllerMap = controllerScanner.getControllers();
            initControllers(controllerMap);
        }
    }

    private void initControllers(Map<Class<?>, Object> controllerMap) {
        for (Map.Entry<Class<?>, Object> controllerEntry : controllerMap.entrySet()) {
            initMethod(controllerMap, controllerEntry);
        }
    }

    private void initMethod(Map<Class<?>, Object> controllerMap,
        Map.Entry<Class<?>, Object> controllerEntry) {
        Set<Method> methods = ReflectionUtils.getAllMethods(
            controllerEntry.getKey(), ReflectionUtils.withAnnotation(RequestMapping.class)
        );
        for(Method method : methods) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            addHandlerExecutions(controllerMap.get(controllerEntry.getKey()), method, requestMapping);
        }
    }

    private void addHandlerExecutions(Object declaredObject, Method method, RequestMapping requestMapping) {
       String url = requestMapping.value();
       List<HandlerKey> handlerKeys = mapHandlerKeys(url, requestMapping.method());
       for (HandlerKey handlerKey : handlerKeys) {
           handlerExecutions.put(handlerKey, new HandlerExecution(declaredObject, method));
       }
    }
}
