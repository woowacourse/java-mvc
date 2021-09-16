package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import nextstep.mvc.exception.MvcException;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);
    private static final String SLASH = "/";

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        try {
            registerHandlerExecutions();
        } catch (ReflectiveOperationException exception) {
            log.error(exception.getMessage());
            throw new MvcException("AnnotationHandlerMapping 초기화에 실패했습니다.");
        }

        log.info("Initialized AnnotationHandlerMapping!");
        handlerExecutions.forEach(
                (path, value) ->
                        log.info("Path : {}, handlerExecution : {}", path, handlerExecutions.get(path).getClass())
        );
    }

    private void registerHandlerExecutions() throws ReflectiveOperationException {
        Set<Class<?>> controllerClasses = findClassesContainsControllerAnnotation();
        for (Class<?> controllerClass : controllerClasses) {
            this.handlerExecutions.putAll(extractHandlerExecutions(controllerClass));
        }
    }

    private Set<Class<?>> findClassesContainsControllerAnnotation() {
        Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private Map<HandlerKey, HandlerExecution> extractHandlerExecutions(Class<?> controllerClass)
            throws ReflectiveOperationException {

        String defaultUrl = defaultUrl(controllerClass);
        Object instance = controllerClass.getDeclaredConstructor().newInstance();
        Method[] methods = controllerClass.getDeclaredMethods();
        return extractAsMethods(methods, defaultUrl, instance);
    }

    private String defaultUrl(Class<?> controllerClass) {
        String defaultUri = controllerClass.getAnnotation(Controller.class).value();
        if (controllerClass.isAnnotationPresent(RequestMapping.class)) {
            defaultUri = controllerClass.getAnnotation(RequestMapping.class).value();
        }
        return defaultUri;
    }

    private Map<HandlerKey, HandlerExecution> extractAsMethods(Method[] methods, String defaultUri, Object instance) {
        Map<HandlerKey, HandlerExecution> handlerExecutions = new HashMap<>();
        for (Method method : methods) {
            if (!method.isAnnotationPresent(RequestMapping.class)) {
                continue;
            }
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            String detailedUri = requestMapping.value();
            RequestMethod[] requestMethods = requestMapping.method();
            HandlerExecution handlerExecution = new HandlerExecution(method, instance);
            String url = makeCompleteUri(defaultUri, detailedUri);
            handlerExecutions.putAll(extractAsRequestMethods(requestMethods, url, handlerExecution));
        }
        return handlerExecutions;
    }

    private String makeCompleteUri(String defaultUri, String detailedUri) {
        String uri = concatSlashIfNeeded(defaultUri) + concatSlashIfNeeded(detailedUri);
        if (uri.isEmpty()) {
            return SLASH;
        }
        return uri;
    }

    private String concatSlashIfNeeded(String uri) {
        if (uri.isEmpty() || uri.startsWith(SLASH)) {
            return uri;
        }
        return SLASH + uri;
    }

    private Map<HandlerKey, HandlerExecution> extractAsRequestMethods(
            RequestMethod[] requestMethods, String url, HandlerExecution handlerExecution) {

        Map<HandlerKey, HandlerExecution> handlerExecutions = new HashMap<>();
        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(url, requestMethod);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
        return handlerExecutions;
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        String url = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(url, requestMethod);
        return handlerExecutions.get(handlerKey);
    }
}
