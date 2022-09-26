package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

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
        final ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        final Map<Class<?>, Object> controllers = controllerScanner.getControllers();
        final Set<Method> requestMappingMethods = getRequestMappingMethods(controllers.keySet());

        for (Method method : requestMappingMethods) {
            if (!method.isAnnotationPresent(RequestMapping.class)) {
                continue;
            }

            addHandlerExecutions(controllers, method, method.getAnnotation(RequestMapping.class));
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void addHandlerExecutions(final Map<Class<?>, Object> controllers, final Method method,
                                      final RequestMapping requestMapping) {

        final List<HandlerKey> handlerKeys = mapHandlerKeys(requestMapping.value(), requestMapping.method());
        final Class<?> controller = method.getDeclaringClass();

        for (HandlerKey handlerKey : handlerKeys) {
            handlerExecutions.put(handlerKey, new HandlerExecution(controllers.get(controller), method));
        }
    }

    private List<HandlerKey> mapHandlerKeys(final String path, final RequestMethod[] methods) {
        return Arrays.stream(methods)
                .map(method -> new HandlerKey(path, method))
                .collect(Collectors.toList());
    }

    private Set<Method> getRequestMappingMethods(Set<Class<?>> controllers) {
        Set<Method> methods = new HashSet<>();
        for (Class<?> controller : controllers) {
            methods.addAll(List.of(controller.getMethods()));
        }
        return methods;
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        final String uri = request.getRequestURI();
        final String method = request.getMethod();

        return handlerExecutions.get(new HandlerKey(uri, RequestMethod.find(method)));
    }
}
