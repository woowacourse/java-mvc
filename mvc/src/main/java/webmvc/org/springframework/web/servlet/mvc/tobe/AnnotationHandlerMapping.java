package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
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
            log.info("Initialized AnnotationHandlerMapping!");
            String packageName = (String) basePackage[0];
            Set<Class<?>> controllerClasses = findAllControllerClasses(packageName);
            controllerClasses.forEach(this::addHandlerExecution);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private Set<Class<?>> findAllControllerClasses(String packageName) {
        Reflections reflections = new Reflections(packageName);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

    private void addHandlerExecution(Class<?> clazz) {
        List<Method> methods = getAnnotatedMethods(clazz);
        Object controller = createInstance(clazz);
        methods.forEach(method -> addHandlerExecution(method, controller));
    }

    private List<Method> getAnnotatedMethods(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toList());
    }

    private Object createInstance(Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getConstructor();
            return constructor.newInstance();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("컨트롤러에 예상치 못한 문제가 발생했습니다");
        }
    }

    private void addHandlerExecution(Method method, Object controller) {
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        String uri = annotation.value();
        List<RequestMethod> requestMethods = Arrays.asList(annotation.method());
        requestMethods.forEach(requestMethod -> addHandlerExecution(uri, requestMethod, controller, method));
    }

    private void addHandlerExecution(String uri, RequestMethod requestMethod, Object controller, Method method) {
        HandlerKey handlerKey = new HandlerKey(uri, requestMethod);
        HandlerExecution handlerExecution = new HandlerExecution(controller, method);
        handlerExecutions.put(handlerKey, handlerExecution);
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        HandlerKey handlerKey = new HandlerKey(requestURI, RequestMethod.valueOf(method));
        return handlerExecutions.get(handlerKey);
    }
}
