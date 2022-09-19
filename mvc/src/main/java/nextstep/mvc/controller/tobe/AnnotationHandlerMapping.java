package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
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
    }

    public Object getHandler(final HttpServletRequest request) {
        for (Object o : basePackage) {
            final Reflections reflections = new Reflections(o);
            final Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Controller.class);
            log.info("{}", typesAnnotatedWith);

            for (Class<?> aClass : typesAnnotatedWith) {
                final Method[] declaredMethods = aClass.getDeclaredMethods();
                for (Method declaredMethod : declaredMethods) {
                    final RequestMapping requestMapping = declaredMethod.getAnnotation(RequestMapping.class);
                    if (requestMapping != null) {
                        final HandlerKey handlerKey = new HandlerKey(requestMapping.value(),
                                RequestMethod.valueOf(requestMapping.method()[0].name()));
                        final Object handler;
                        try {
                            handler = declaredMethod.getDeclaringClass().getConstructor().newInstance();
                        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                            throw new RuntimeException("매핑 가능한 핸들러가 존재하지 않습니다.");
                        }
                        handlerExecutions.put(handlerKey, new HandlerExecution(handler, declaredMethod));
                    }
                }
            }
        }

        return handlerExecutions.get(new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod())));
    }
}
