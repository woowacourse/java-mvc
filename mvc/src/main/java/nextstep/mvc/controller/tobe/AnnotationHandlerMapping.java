package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
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
        log.info("Initialized AnnotationHandlerMapping!");

        Set<Class<?>> controllers = findAllControllers(new Reflections(basePackage));
        List<Method> methods = filterPresentRequestMapping(findAllMethods(controllers));
        saveHandlers(methods);
    }

    private Set<Class<?>> findAllControllers(Reflections reflections) {
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private List<Method> findAllMethods(Set<Class<?>> controllers) {
        return controllers.stream()
            .map(Class::getMethods)
            .flatMap(Arrays::stream)
            .collect(Collectors.toList());
    }

    private List<Method> filterPresentRequestMapping(List<Method> methods) {
        return methods.stream()
            .filter(it -> it.isAnnotationPresent(RequestMapping.class))
            .collect(Collectors.toList());
    }

    private void saveHandlers(List<Method> methods) {
        for (Method method : methods) {
            saveHandler(method);
        }
    }

    private void saveHandler(Method method) {
        if (!method.isAnnotationPresent(RequestMapping.class)) {
            return;
        }
        List<HandlerKey> handlerKeys = extractToRequestMapping(method);
        HandlerExecution handlerExecution = new HandlerExecution(method);
        for (HandlerKey handlerKey : handlerKeys) {
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    private List<HandlerKey> extractToRequestMapping(Method method) {
        RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
        String url = requestMapping.value();
        RequestMethod[] requestMethods = requestMapping.method();
        return Arrays.stream(requestMethods)
            .map(requestMethod -> new HandlerKey(url, requestMethod))
            .collect(Collectors.toList());
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        String uri = request.getRequestURI();
        RequestMethod method = RequestMethod.valueOf(request.getMethod());

        HandlerKey handlerKey = new HandlerKey(uri, method);
        return handlerExecutions.get(handlerKey);
    }
}
