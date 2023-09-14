package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Ref;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        log.info("Initialized AnnotationHandlerMapping!");

        for (Object object : basePackage) {
            final Reflections reflections = new Reflections((String) object);
            Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Controller.class);
            for (Class<?> aClass : typesAnnotatedWith) {
                Method[] methods = aClass.getDeclaredMethods();
                Constructor<?> constructor = aClass.getConstructor();
                Object o = constructor.newInstance();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(RequestMapping.class)) {
                        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                        HandlerExecution handlerExecution = new HandlerExecution(method, o);
                        String value = annotation.value();
                        RequestMethod[] requestMethods = annotation.method();
                        for (RequestMethod requestMethod : requestMethods) {
                            HandlerKey handlerKey = new HandlerKey(value, requestMethod);
                            handlerExecutions.put(handlerKey, handlerExecution);
                        }
                    }
                }
            }
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey1 = handlerExecutions.keySet().stream()
                .filter(handlerKey -> {
                    return Objects.equals(request.getRequestURI(), handlerKey.getUrl())
                            && Objects.equals(request.getMethod().toLowerCase(), handlerKey.getRequestMethod().name().toLowerCase());
                })
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("404"));
        return handlerExecutions.get(handlerKey1);
    }
}
