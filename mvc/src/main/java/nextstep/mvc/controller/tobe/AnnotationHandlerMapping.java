package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final String[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final String... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .forPackages(basePackage)
                .setScanners(Scanners.values()));

        Set<Method> methods = reflections.getMethodsAnnotatedWith(RequestMapping.class);

        for (Method method : methods) {
            Class<?> declaringClass = method.getDeclaringClass();
            try {
                Constructor<?> constructor = declaringClass.getConstructor();
                Object instance = constructor.newInstance();
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMapping.method()[0]);
                HandlerExecution handlerExecution = new HandlerExecution(instance, method);
                handlerExecutions.put(handlerKey, handlerExecution);
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                     IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }
}
