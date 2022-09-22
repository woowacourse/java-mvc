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
import nextstep.mvc.controller.tobe.exception.ControllerNotFoundException;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
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

    public void initialize() {
        Set<Class<?>> controllerClasses = extractClasses();
        List<Method> methods = extractMethods(controllerClasses);

        for (Method method : methods) {
            addHandlerExecutions(method);
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private Set<Class<?>> extractClasses() {
        Reflections classReflections = new Reflections(basePackage, Scanners.TypesAnnotated);
        return classReflections.getTypesAnnotatedWith(Controller.class);
    }

    private List<Method> extractMethods(final Set<Class<?>> controllers) {
        return controllers.stream()
                .flatMap(it -> Arrays.stream(it.getMethods()))
                .filter(it -> it.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toList());
    }

    private void addHandlerExecutions(final Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        List<HandlerKey> handlerKeys = HandlerKey.from(requestMapping);
        HandlerExecution handlerExecution = new HandlerExecution(method);

        for (HandlerKey handlerKey : handlerKeys) {
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request);
        HandlerExecution handlerExecution = handlerExecutions.get(handlerKey);

        if (handlerExecution == null) {
            throw new ControllerNotFoundException();
        }

        return handlerExecution;
    }
}
