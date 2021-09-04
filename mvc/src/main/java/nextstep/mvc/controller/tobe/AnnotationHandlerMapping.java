package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Set;
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

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        for (Object obj : basePackage) {
            Reflections reflections = new Reflections((String) obj);

            Set<Class<?>> controllerClass = reflections.getTypesAnnotatedWith(Controller.class);

            for(Class<?> clazz : controllerClass) {
                log.debug("{} ", clazz.getSimpleName());

                Method[] methods = clazz.getDeclaredMethods();

                for (Method method : methods) {
                    if (method.isAnnotationPresent(RequestMapping.class)) {
                        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                        String value = annotation.value();
                        RequestMethod[] method1 = annotation.method();

                        for (RequestMethod requestMethod : method1) {
                            try {
                                Object object = clazz.getConstructor().newInstance();
                                handlerExecutions.put(new HandlerKey(value, requestMethod), new HandlerExecution(object, method));
                            } catch (Exception e) {

                            }

                        }
                    }
                }
            }
        }
    }

    public Object getHandler(HttpServletRequest request) {
        return handlerExecutions.get(new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod())));
    }
}
