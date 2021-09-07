package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.checkerframework.checker.units.qual.A;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
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
        return handlerExecutions.get(new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod())));
    }

    public Set<Method> getRequestMappingMethods(Set<Class<?>> controllers) {
        Set<Method> methods = new HashSet<>();
        for (Class<?> controller : controllers) {
            methods.addAll(Arrays.asList(controller.getMethods()));
        }
        return methods;
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

            for (Map.Entry<Class<?>, Object> controllerEntry : controllerMap.entrySet()) {
                Set<Method> methods = ReflectionUtils.getAllMethods(controllerEntry.getKey(), ReflectionUtils.withAnnotation(RequestMapping.class));
                for(Method method : methods) {
                    RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                    addHandlerExecutions(controllerMap.get(controllerEntry.getKey()), method, requestMapping);
                }
            }
        }
    }

    public void addHandlerExecutions(Object declaredObject, Method method, RequestMapping requestMapping) {
       String url = requestMapping.value();
       List<HandlerKey> handlerKeys = mapHandlerKeys(url, requestMapping.method());
       for (HandlerKey handlerKey : handlerKeys) {
           handlerExecutions.put(handlerKey, new HandlerExecution(declaredObject, method));
       }
    }
}
