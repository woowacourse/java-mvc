package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackages = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        log.info("Initialized AnnotationHandlerMapping!");
        Set<Class<?>> controllers = new HashSet<>();
        Reflections reflections;
        for (Object basePackage : basePackages) {
            reflections = new Reflections(basePackage);
            controllers.addAll(reflections.getTypesAnnotatedWith(Controller.class));
        }

        for (Class controller : controllers) {
            Object object = controller.getDeclaredConstructor().newInstance();
            Set<Method> requestMappingMethods = ReflectionUtils.getAllMethods(controller, ReflectionUtils.withAnnotation(RequestMapping.class));
            for (Method method : requestMappingMethods) {
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                RequestMethod[] requestMethods = requestMapping.method();
                for (RequestMethod requestMethod : requestMethods) {
                    handlerExecutions.put(new HandlerKey(requestMapping.value(), requestMethod),
                            new HandlerExecution(object, method));
                }
            }
        }
    }

    public Object getHandler(HttpServletRequest request)
    {
        return handlerExecutions.get(new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod())));
    }
}
