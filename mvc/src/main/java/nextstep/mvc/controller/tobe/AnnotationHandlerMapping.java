package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.ReflectionUtils;
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
        for (Object bp : basePackage) {
            Reflections reflections = new Reflections(bp.toString());
            Set<Class<?>> handlers = reflections.getTypesAnnotatedWith(Controller.class);
            addHandlerExecutions(handlers);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void addHandlerExecutions(final Set<Class<?>> handlers) {
        for (Class<?> hdl : handlers) {
            Object handler = createHandler(hdl);

            Set<Method> methods = ReflectionUtils.getMethods(
                    handler.getClass(),
                    method -> method.isAnnotationPresent(RequestMapping.class)
            );

            for (Method mtd : methods) {
                List<HandlerKey> handlerKeys = createHandlerKeys(mtd);
                for (HandlerKey handlerKey : handlerKeys) {
                    HandlerMethod handlerMethod = new HandlerMethod(handler, mtd);
                    handlerExecutions.put(handlerKey, new HandlerExecution(handlerMethod));
                }
            }
        }
    }

    private Object createHandler(final Class<?> handlerClass) {
        try {
            Constructor<?> constructor = handlerClass.getDeclaredConstructor();
            return constructor.newInstance();
        } catch (NoSuchMethodException | InvocationTargetException |
                 InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private List<HandlerKey> createHandlerKeys(final Method method) {
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        String path = annotation.value();
        return Arrays.stream(annotation.method())
                .map(m -> new HandlerKey(path, m))
                .collect(Collectors.toList());
    }

    public Object getHandler(final HttpServletRequest request) {
        try {
            HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
            return handlerExecutions.get(handlerKey);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("[ERROR] HandlerExecution 이 존재하지 않습니다.");
        }
    }
}
