package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import nextstep.mvc.util.AnnotationScanner;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        for (Object packagePath : basePackage) {
            Set<Class<?>> classes = AnnotationScanner.scanClass(packagePath, Controller.class);
            enrollHandlerWithRequestMapping(classes);
        }
    }

    private void enrollHandlerWithRequestMapping(Set<Class<?>> classes) {
        for (Class<?> aClass : classes) {
            Object clazz = null;
            try {
                clazz = aClass.getDeclaredConstructor().newInstance();
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
            enrollRequestMappingMethods(clazz, aClass);
        }
    }

    private void enrollRequestMappingMethods(Object clazz, Class<?> aClass) {
        Set<Method> methods = AnnotationScanner.scanMethod(aClass, RequestMapping.class);
        for (Method method : methods) {
            RequestMapping annotation = method.getAnnotation(RequestMapping.class);
            for (RequestMethod requestMethod : annotation.method()) {
                handlerExecutions.put(
                        new HandlerKey(annotation.value(), requestMethod),
                        new HandlerExecution(clazz, method)
                );
            }
        }
    }

    public Object getHandler(HttpServletRequest request) {
        HandlerKey key = new HandlerKey(
                request.getRequestURI(),
                RequestMethod.valueOf(request.getMethod())
        );

        return handlerExecutions.get(key);
    }
}
