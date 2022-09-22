package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
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
        Set<Class<?>> controllers = new HashSet<>(reflections.getTypesAnnotatedWith(Controller.class));

        for (Class<?> controller : controllers) {
            List<Method> controllerMethods = Arrays.stream(controller.getDeclaredMethods())
                    .filter(it -> it.isAnnotationPresent(RequestMapping.class))
                    .collect(Collectors.toList());
            addHandlers(controller, controllerMethods);
        }
    }

    private void addHandlers(Class<?> controllerClass, List<Method> controllerMethods) {
        Object controller = makeInstance(controllerClass);
        for (Method method : controllerMethods) {
            addHandler(controller, method);
        }
    }

    private Object makeInstance(Class<?> controllerClass) {
        try {
            return controllerClass.getDeclaredConstructor()
                    .newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        throw new IllegalStateException("알수없는 이유로 인스턴스를 생성할 수 없습니다!");
    }

    private void addHandler(Object controller, Method method) {
        RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
        String uri = requestMapping.value();
        RequestMethod[] httpMethods = requestMapping.method();
        for (RequestMethod httpMethod : httpMethods) {
            HandlerKey handlerKey = new HandlerKey(uri, httpMethod);
            HandlerExecution handlerExecution = new HandlerExecution(controller, method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String uri = request.getRequestURI();
        RequestMethod httpMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(uri, httpMethod);
        return handlerExecutions.get(handlerKey);
    }
}
