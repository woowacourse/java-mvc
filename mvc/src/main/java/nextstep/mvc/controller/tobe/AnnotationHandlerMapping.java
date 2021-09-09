package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.ReflectionUtils;
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
        log.info("Initialized AnnotationHandlerMapping!");
        Arrays.stream(basePackage).forEach(obj -> {
            String packagePrefix = (String) obj;
            ControllerScanner controllerScanner = new ControllerScanner(packagePrefix);
            Map<Class<?>, Object> controllerMap = controllerScanner.getControllers();
            initControllers(controllerMap);
        });
    }

    private void initControllers(Map<Class<?>, Object> controllers) {
        controllers.entrySet().forEach(entry -> initMethod(controllers, entry));
    }

    private void initMethod(Map<Class<?>, Object> controllers,
        Entry<Class<?>, Object> controllerEntry) {
        final Set<Method> methods = ReflectionUtils.getAllMethods(
            controllerEntry.getKey(), ReflectionUtils.withAnnotation(RequestMapping.class)
        );
        methods.forEach(method -> {
            final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            addHandlerExecutions(controllers.get(controllerEntry.getKey()), method, requestMapping);
        });

    }

    private void addHandlerExecutions(Object controller, Method method,
        RequestMapping requestMapping) {
        final String url = requestMapping.value();
        final List<HandlerKey> handlerKeys = mapHandlerKeys(url, requestMapping.method());
        handlerKeys.forEach(handlerKey -> handlerExecutions
            .put(handlerKey, new HandlerExecution(controller, method)));
    }

    private List<HandlerKey> mapHandlerKeys(String url, RequestMethod[] methods) {
        final List<HandlerKey> handlerKeys = new ArrayList<>();
        Arrays.stream(methods).forEach(method -> handlerKeys.add(new HandlerKey(url, method)));
        return handlerKeys;
    }

    public Object getHandler(HttpServletRequest request) {
        return handlerExecutions.get(
            new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod())));
    }
}
