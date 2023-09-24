package webmvc.org.springframework.web.servlet.mvc;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        Reflections reflections = new Reflections(basePackage);
        Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
        controllerClasses.forEach(this::addControllerHandlers);
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void addControllerHandlers(Class<?> clazz) {
        List<Method> methods = getAnnotatedMethods(clazz, RequestMapping.class);
        Object controller = createInstance(clazz);
        methods.forEach(method -> addHandlerExecutions(controller, method));
    }

    private List<Method> getAnnotatedMethods(Class<?> clazz, Class<? extends Annotation> annotation) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(annotation))
                .collect(Collectors.toList());
    }

    private Object createInstance(Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getConstructor();
            return constructor.newInstance();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalStateException("컨트롤러를 생성하는데 문제가 생겼습니다");
        }
    }

    private void addHandlerExecutions(Object controller, Method method) {
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        HandlerExecution handlerExecution = new HandlerExecution(controller, method);
        Arrays.stream(annotation.method())
                .map(requestMethod -> new HandlerKey(annotation.value(), requestMethod))
                .forEach(handlerKey -> handlerExecutions.put(handlerKey, handlerExecution));
    }

    @Override
    public boolean canHandle(HttpServletRequest request) {
        HandlerKey handlerKey = getHandlerKey(request);
        return handlerExecutions.containsKey(handlerKey);
    }

    private HandlerKey getHandlerKey(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        return new HandlerKey(requestURI, RequestMethod.valueOf(method));
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        HandlerKey handlerKey = getHandlerKey(request);
        return handlerExecutions.get(handlerKey);
    }
}
