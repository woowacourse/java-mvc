package nextstep.mvc.controller.tobe;

import static org.reflections.scanners.Scanners.TypesAnnotated;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> classes = reflections.get(TypesAnnotated.with(Controller.class).asClass());

        for (Class<?> aClass : classes) {
            Method[] methods = aClass.getMethods();
            for (Method method : methods) {
                RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                if (annotation != null) {
                    String value = annotation.value();
                    RequestMethod[] method1 = annotation.method();
                    for (RequestMethod requestMethod : method1) {
                        try{
                            HandlerKey handlerKey = new HandlerKey(value, requestMethod);
                            Constructor<?> constructor = aClass.getConstructor();
                            HandlerExecution handlerExecution = new HandlerExecution(constructor.newInstance(), method);
                            handlerExecutions.put(handlerKey, handlerExecution);
                        }
                        catch (Exception exception) {
                            throw new IllegalArgumentException("reflection 중 문제가 발생했습니다.");
                        }
                    }
                }
            }
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String method = request.getMethod();
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(method));
        return handlerExecutions.get(handlerKey);
    }
}
