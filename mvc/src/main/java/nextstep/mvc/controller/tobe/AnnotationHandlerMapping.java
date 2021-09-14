package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import nextstep.mvc.controller.ControllerScanner;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping {
    private static final Logger LOG = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        LOG.info("Initialized AnnotationHandlerMapping!");
        Arrays.stream(basePackage)
                .forEach(packageName -> scanPackage((String) packageName));
    }

    private void scanPackage(String packageName) {
        try {
            ControllerScanner controllerScanner = new ControllerScanner(packageName);
            controllerScanner.getControllers().forEach(entry -> {
                findAllMethodsOfController(entry);
                LOG.info("Controller : {}",  entry.getKey().getName());
            });
            LOG.info("Initialized Annotation Handler Mapping!");
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void findAllMethodsOfController(Map.Entry<Class<?>, Object> controller) {
        Set<Method> allMethods = ReflectionUtils.getAllMethods(controller.getKey(), ReflectionUtils.withAnnotation(RequestMapping.class));
        allMethods.forEach(method -> addHandler(controller, method));
    }

    private void addHandler(Map.Entry<Class<?>, Object> controller, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        Arrays.stream(requestMapping.method())
                .forEach(requestMethod -> {
                    HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
                    HandlerExecution handlerExecution = new HandlerExecution(controller.getValue(), method);
                    handlerExecutions.put(handlerKey, handlerExecution);
                });
    }

    public Object getHandler(HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}
