package nextstep.mvc.controller.tobe;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {
    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);
        Map<Class<?>, Object> controllers = controllerScanner.getControllers();

        controllers.forEach((controllerClass, object) -> {
                    List<Method> methodsAnnotatedWithRequestMapping = getMethodsAnnotatedWithRequestMapping(controllerClass);
                    for (Method method : methodsAnnotatedWithRequestMapping) {
                        createHandlerExecution(object, method);
                    }
                }
        );
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private List<Method> getMethodsAnnotatedWithRequestMapping(Class<?> controllerClass) {
        Method[] declaredMethods = controllerClass.getDeclaredMethods();
        return Arrays.stream(declaredMethods)
                     .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                     .collect(Collectors.toList());
    }

    private void createHandlerExecution(Object object, Method method) {
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        String requestUri = annotation.value();
        RequestMethod[] requestMethods = annotation.method();

        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(requestUri, requestMethod);
            handlerExecutions.put(handlerKey, new HandlerExecution(object, method));
        }
    }

    public Object getHandler(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        log.debug("Request Mapping Uri : {} , Request Method : {}", requestUri, requestMethod);

        HandlerKey handlerKey = new HandlerKey(requestUri, requestMethod);
        return handlerExecutions.get(handlerKey);
    }
}
