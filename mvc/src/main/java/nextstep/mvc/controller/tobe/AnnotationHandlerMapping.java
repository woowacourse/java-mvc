package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    @Override
    public void initialize() {
        try {
            log.info("Initialized AnnotationHandlerMapping!");
            registerHandlerMapping();

            handlerExecutions.keySet()
                    .forEach(handlerKey -> log.debug("등록된 handler: {}", handlerKey));
        } catch (Exception e) {
            log.error("Failed AnnotationHandlerMapping Error MSG= {}", e.getMessage());
        }
    }

    private void registerHandlerMapping() throws Exception {
        Set<Class<?>> controllers = ControllerScanner.scanClassByAnnotation(basePackage, Controller.class);
        for (Class<?> controller : controllers) {
            registerRequestMappingMethods(controller);
        }
    }

    private void registerRequestMappingMethods(Class<?> controller) throws Exception {
        Set<Method> methods = ControllerScanner.scanMethodByClass(controller, RequestMapping.class);
        for (Method handler : methods) {
            registerRequestMappingMethod(controller, handler);
        }
    }

    private void registerRequestMappingMethod(Class<?> controller, Method handler) throws Exception {
        RequestMapping requestMapping = handler.getAnnotation(RequestMapping.class);
        for (RequestMethod requestMethod : requestMapping.method()) {
            HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
            Object instance = controller.getDeclaredConstructor().newInstance();
            HandlerExecution handlerExecution = new HandlerExecution(handler, instance);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        HandlerKey handlerKey = HandlerKey.of(request);
        return handlerExecutions.get(handlerKey);
    }
}
