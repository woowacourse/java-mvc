package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.reflections.ReflectionUtils;
import org.reflections.util.ReflectionUtilsPredicates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final String[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final String... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = new ConcurrentHashMap<>();
    }

    @Override
    public void initialize() {
        try {
            for (String basePackage : basePackages) {
                ControllerScanner controllerScanner = new ControllerScanner(basePackage);
                scanControllers(controllerScanner.getControllers(ScanType.ANNOTATION));
            }
            log.info("Initialized AnnotationHandlerMapping: {} handlers", handlerExecutions.size());
        } catch (ReflectiveOperationException e) {
            log.warn("Initialize failed: {}", e.getMessage());
        }
    }

    private void scanControllers(Map<Class<?>, Object> controllers) {
        for (Entry<Class<?>, Object> controllerEntry : controllers.entrySet()) {
            Set<Method> methods = ReflectionUtils.getAllMethods(
                    controllerEntry.getKey(),
                    ReflectionUtilsPredicates.withAnnotation(RequestMapping.class)
            );
            scanControllerMethods(controllerEntry.getValue(), methods);
        }
    }

    private void scanControllerMethods(Object controller, Set<Method> methods) {
        for (Method method : methods) {
            registerRequestMappingMethod(controller, method);
        }
    }

    private void registerRequestMappingMethod(Object controller, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        String url = requestMapping.value();
        HandlerKey key = new HandlerKey(url, requestMapping.method());
        handlerExecutions.put(key, new HandlerExecution(controller, method));
        log.info("Annotation Mapping Initialized: {} - {}", key, method.getName());
    }

    @Override
    public HandlerExecution getHandler(final HttpServletRequest request) {
        String uri = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());

        HandlerKey key = new HandlerKey(uri, requestMethod);
        return handlerExecutions.get(key);
    }
}
