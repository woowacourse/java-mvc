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
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> controllers = controllerScanner.scanControllers();
        registerHandlerExecutions(controllers);

        log.info("Initialized AnnotationHandlerMapping!");
        handlerExecutions.forEach(
                (path, value) ->
                        log.info("Path : {}, handlerExecution : {}", path, handlerExecutions.get(path).getClass())
        );
    }

    private void registerHandlerExecutions(Map<Class<?>, Object> controllers) {
        for (Map.Entry<Class<?>, Object> controller : controllers.entrySet()) {
            Class<?> aClass = controller.getKey();
            Method[] methods = aClass.getDeclaredMethods();
            Object instance = controller.getValue();
            String defaultUri = defaultUri(aClass);
            registerFromMethods(methods, instance, defaultUri);
        }
    }

    private String defaultUri(Class<?> controllerClass) {
        String defaultUri = controllerClass.getAnnotation(Controller.class).value();
        if (controllerClass.isAnnotationPresent(RequestMapping.class)) {
            defaultUri = controllerClass.getAnnotation(RequestMapping.class).value();
        }
        return defaultUri;
    }

    private void registerFromMethods(Method[] methods, Object instance, String defaultUri) {
        for (Method method : methods) {
            if (!method.isAnnotationPresent(RequestMapping.class)) {
                continue;
            }
            registerFromMethod(method, instance, defaultUri);
        }
    }

    private void registerFromMethod(Method method, Object instance, String defaultUri) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        String detailedUri = requestMapping.value();
        String completeUri = makeCompleteUri(defaultUri, detailedUri);
        RequestMethod[] requestMethods = requestMapping.method();
        HandlerExecution handlerExecution = new HandlerExecution(method, instance);

        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(completeUri, requestMethod);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
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

    @Override
    public Object getHandler(HttpServletRequest request) {
        String uri = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(uri, requestMethod);
        return handlerExecutions.get(handlerKey);
    }
}
