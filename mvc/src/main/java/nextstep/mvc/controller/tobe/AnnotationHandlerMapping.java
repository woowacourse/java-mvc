package nextstep.mvc.controller.tobe;

import static java.util.stream.Collectors.toList;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nextstep.mvc.ComponentScanner;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Map<HandlerKey, HandlerExecution> handlerExecutions;
    private final ComponentScanner componentScanner;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.handlerExecutions = new HashMap<>();
        this.componentScanner = new ComponentScanner(basePackage);
    }

    @Override
    public void initialize() {
        Map<String, Object> controllers = componentScanner.getComponent(Controller.class);
        List<Method> requestMappingMethods = getRequestMappingMethods(controllers);
        requestMappingMethods.forEach(method -> addHandlerExecution(controllers, method));

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private List<Method> getRequestMappingMethods(Map<String, Object> controllers) {
        return controllers.values().stream()
            .flatMap(controller ->
                ReflectionUtils.getAllMethods(
                    controller.getClass(), ReflectionUtils.withAnnotation(RequestMapping.class)
                ).stream()
            )
            .collect(toList());
    }

    private void addHandlerExecution(Map<String, Object> controllers, Method method) {
        RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);

        Arrays.stream(requestMapping.method())
            .forEach(requestMethod -> {
                handlerExecutions.put(
                    new HandlerKey(requestMapping.value(), requestMethod),
                    new HandlerExecution(
                        controllers.get(method.getDeclaringClass().getSimpleName()),
                        method
                    )
                );

                log.info("Path : {}, Controller : {}, Method : {}",
                    requestMapping.value(),
                    method.getDeclaringClass().getSimpleName(),
                    method.getName()
                );
            });
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        HandlerKey handlerKey =
            new HandlerKey(
                request.getRequestURI(),
                RequestMethod.valueOf(request.getMethod())
            );

        return handlerExecutions.get(handlerKey);
    }
}
