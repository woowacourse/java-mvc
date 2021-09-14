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
import java.util.Map;
import java.util.Set;

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
        Reflections reflections = new Reflections("controller");
        Set<Class<?>> controllerClass = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> aClass : controllerClass) {
            try {
                Object clazz = aClass.getDeclaredConstructor().newInstance();
                Set<Method> methods = ReflectionUtils.getAllMethods(aClass, ReflectionUtils.withAnnotation(RequestMapping.class));
                for (Method method : methods) {
                    RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                    handlerExecutions.put(
                            new HandlerKey(annotation.value(), annotation.method()[0]),
                            new HandlerExecution(clazz, method)
                    );
                }
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    public Object getHandler(HttpServletRequest request) {
        HandlerKey key = new HandlerKey(
                request.getRequestURI(),
                RequestMethod.requestMethod(request.getMethod())
        );

        return handlerExecutions.get(key);
    }
}
