package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
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
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping{

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        final Reflections reflections = new Reflections(basePackage);
        final Set<Class<?>> controllerAnnotatedClasses = reflections.getTypesAnnotatedWith(Controller.class);
        controllerAnnotatedClasses.forEach(clazz -> {
            if (clazz.isAnnotationPresent(RequestMapping.class)) {
                final RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
                addHandlerExecution(requestMapping.value(), clazz);
                return;
            }
            addHandlerExecution(clazz);
        });
        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void addHandlerExecution(final Class<?> clazz) {
        addHandlerExecution("", clazz);
    }

    private void addHandlerExecution(final String prefixPath, final Class<?> clazz) {
        Arrays.stream(clazz.getMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .forEach(method -> {
                    final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                    final HandlerKey handlerKey = new HandlerKey(prefixPath + requestMapping.value(), requestMapping.method()[0]);
                    final HandlerExecution handler = createHandler(clazz, method);
                    handlerExecutions.put(handlerKey, handler);
                });
    }

    private HandlerExecution createHandler(final Class<?> clazz, final Method method) {
        try {
            return new HandlerExecution(clazz.getConstructor().newInstance(), method);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            log.error("핸들러 생성 실패");
            throw new RuntimeException("핸들러를 생성할 수 없습니다.");
        }
    }


    @Override
    public Object getHandler(final HttpServletRequest request) {
        return this.handlerExecutions.get(new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod())));
    }
}
