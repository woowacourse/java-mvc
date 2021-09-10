package nextstep.mvc.mapping;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.mvc.HandlerMapping;
import nextstep.mvc.controller.scanner.ControllerScanner;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.controller.tobe.HandlerKey;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final String[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(String... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        Set<Class<?>> controllers = ControllerScanner.scanController(basePackages);
        for (Class<?> controller : controllers) {
            List<Method> requestMappingMethods = scanRequestMappingMethod(controller);
            Object controllerInstance = getControllerInstance(controller);

            for (Method requestMappingMethod : requestMappingMethods) {
                RequestMapping annotation = requestMappingMethod.getDeclaredAnnotation(RequestMapping.class);
                String url = annotation.value();

                for (RequestMethod requestMethod : annotation.method()) {
                    handlerExecutions.put(new HandlerKey(url, requestMethod),
                        new HandlerExecution(controllerInstance, requestMappingMethod)
                    );
                }
            }
        }
    }

    private List<Method> scanRequestMappingMethod(Class<?> controller) {
        return Arrays.stream(controller.getDeclaredMethods())
            .filter(method -> method.isAnnotationPresent(RequestMapping.class))
            .collect(Collectors.toList());
    }

    private Object getControllerInstance(Class<?> controller) {
        try {
            return controller.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalArgumentException();
        }
    }

    public Object getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod().toUpperCase());
        log.debug("Request Mapping Uri : {}", requestURI);
        return handlerExecutions.get(new HandlerKey(requestURI, requestMethod));
    }
}
