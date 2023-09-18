package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void init() throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllerReflections = reflections.getTypesAnnotatedWith(context.org.springframework.stereotype.Controller.class);
        for (Class<?> controllerReflection : controllerReflections) {
            addHandlerWithTargetMethods(controllerReflection);
        }
    }

    private void addHandlerWithTargetMethods(Class<?> controllerReflection) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        List<Method> methods = Arrays.stream(controllerReflection.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toList());

        for (Method method : methods) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            String value = requestMapping.value();
            addKeyAndHandler(controllerReflection, requestMapping, value, method);
        }
    }

    private void addKeyAndHandler(Class<?> controllerReflection, RequestMapping requestMapping, String value, Method method) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        RequestMethod[] requestMethods = requestMapping.method();
        Object controller = controllerReflection.getDeclaredConstructor().newInstance();

        for (RequestMethod requestMethod : requestMethods) {
            HandlerExecution handlerExecution = new HandlerExecution(controller, method);
            handlerExecutions.put(new HandlerKey(value, requestMethod), handlerExecution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        HandlerExecution handlerExecution = handlerExecutions.get(handlerKey);
        if (handlerExecution == null) {
            throw new IllegalArgumentException("요청에 해당하는 메서드가 없습니다.");
        }
        return handlerExecution;
    }
}
