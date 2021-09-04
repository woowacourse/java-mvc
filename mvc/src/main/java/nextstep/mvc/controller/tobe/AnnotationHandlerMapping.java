package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import nextstep.mvc.controller.exception.HandlerMappingInitiationException;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Start Initialing AnnotationHandlerMapping!");

        for (Object basePackage : basePackages) {
            log.info("Register HandlerExecution from package: {}", basePackage);
            registerHandlerMapping(basePackage);
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void registerHandlerMapping(Object packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> handlerExecutionClasses = reflections.getTypesAnnotatedWith(RequestMapping.class);

        for (Class<?> handlerExecutionClass : handlerExecutionClasses) {
            RequestMapping annotation = handlerExecutionClass.getAnnotation(RequestMapping.class);

            String url = annotation.value();
            RequestMethod[] methods = annotation.method();

            try {
                HandlerExecution handlerExecution = (HandlerExecution) handlerExecutionClass.getDeclaredConstructor().newInstance();

                for (RequestMethod method : methods) {
                    HandlerKey handlerKey = new HandlerKey(url, method);
                    handlerExecutions.put(handlerKey, handlerExecution);
                    log.info("[{}] {} : {} has been registered.", method, url, handlerExecution);
                }

            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException exception) {
                throw new HandlerMappingInitiationException(exception);
            }
        }
    }

    public Object getHandler(HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}
