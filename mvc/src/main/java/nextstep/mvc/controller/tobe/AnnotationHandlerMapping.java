package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import nextstep.mvc.HandlerMapping;
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

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        for (Object base : basePackage) {
            initializeEachBase(base);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void initializeEachBase(Object base) {
        Reflections reflections = new Reflections(base);
        for (Class<?> controller : reflections.getTypesAnnotatedWith(Controller.class)) {
            List<Method> methods = getRequestMappingMethods(controller);
            methods.forEach(this::setUpHandlerExecutions);
        }
    }

    private List<Method> getRequestMappingMethods(Class<?> controller) {
        return Arrays.stream(controller.getMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toList());
    }

    private void setUpHandlerExecutions(Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        for (RequestMethod requestMethod : requestMapping.method()) {
            HandlerKey key = new HandlerKey(requestMapping.value(), requestMethod);
            HandlerExecution value = new HandlerExecution(method);
            handlerExecutions.put(key, value);
            log.info("Path : {} , Method : {}", requestMapping.value(), requestMethod);
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}
