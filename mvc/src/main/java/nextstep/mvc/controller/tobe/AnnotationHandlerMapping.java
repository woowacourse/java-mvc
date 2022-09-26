package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        final Reflections reflections = new Reflections(basePackage);
        final ControllerScanner controllerScanner = new ControllerScanner(reflections);
        final Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        initHandlerExecutions(controllers);
    }

    private void initHandlerExecutions(Map<Class<?>, Object> controllers) {
        for (Class<?> controller : controllers.keySet()) {
            System.out.println("controller.getName() = " + controller.getName());
            addHandlerExecutions(controllers.get(controller), getRequestMappingMethods(controller));
        }
    }

    private List<Method> getRequestMappingMethods(Class<?> controller) {
        return Stream.of(controller.getDeclaredMethods())
                .filter(x -> x.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toList());
    }

    private void addHandlerExecutions(Object instance, List<Method> methods) {
        for (Method method : methods) {
            final HandlerKey handlerKey = HandlerKey.from(method);
            final HandlerExecution handlerExecution = new HandlerExecution(instance, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        final RequestMethod method = RequestMethod.find(request.getMethod());
        final HandlerKey handlerKey = new HandlerKey(requestURI, method);
        return handlerExecutions.get(handlerKey);
    }
}
