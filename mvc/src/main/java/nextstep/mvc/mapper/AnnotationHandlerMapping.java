package nextstep.mvc.mapper;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.controller.tobe.HandlerKey;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.ReflectionUtils;

public class AnnotationHandlerMapping implements HandlerMapping {

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        Map<Class<?>, Object> controllers = ControllerScanner.getControllers(basePackage);
        initHandlerExecution(controllers);
    }

    private void initHandlerExecution(Map<Class<?>, Object> controllers) {
        for (Map.Entry<Class<?>, Object> controllerEntry : controllers.entrySet()) {
            Class<?> controllerClass = controllerEntry.getKey();
            Set<Method> methods = ReflectionUtils
                .getAllMethods(controllerClass, ReflectionUtils.withAnnotation(RequestMapping.class));
            addHandlerExecutions(controllers, controllerClass, methods);
        }
    }

    private void addHandlerExecutions(Map<Class<?>, Object> controllers, Class<?> controllerClass, Set<Method> methods) {
        for (Method method : methods) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            String url = requestMapping.value();
            RequestMethod[] requestMethods = requestMapping.method();
            List<HandlerKey> handlerKeys = getHandlerKeys(url, requestMethods);
            HandlerExecution handlerExecution = new HandlerExecution(controllers.get(controllerClass), method);
            for (HandlerKey handlerKey : handlerKeys) {
                this.handlerExecutions.put(handlerKey, handlerExecution);
            }
        }
    }

    private List<HandlerKey> getHandlerKeys(String url, RequestMethod[] requestMethods) {
        return Arrays.stream(requestMethods)
            .map(requestMethod -> new HandlerKey(url, requestMethod))
            .collect(Collectors.toList());
    }

    public Object getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod method = RequestMethod.valueOf(request.getMethod().toUpperCase());
        return handlerExecutions.get(new HandlerKey(requestUri, method));
    }
}
