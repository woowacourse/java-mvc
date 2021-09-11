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
import nextstep.mvc.HandlerMapping;
import nextstep.util.ReflectionUtils;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final List<String> basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(String... basePackage) {
        this.basePackage = Arrays.asList(basePackage);
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        Set<Class<?>> controllers = ReflectionUtils
            .scanClassByAnnotationWith(basePackage, Controller.class);
        for (Class<?> controller : controllers) {
            requestMapping(controller);
        }
    }

    private void requestMapping(Class<?> controllerClass) {
        for (Method declaredMethod : ReflectionUtils
            .scanAllMethodByAnnotationWith(controllerClass, RequestMapping.class)) {
            HandlerKey handlerKey = createHandlerKeyFrom(declaredMethod);
            inputHandlerKeyAndHandler(handlerKey,
                new HandlerExecution(declaredMethod, createHandler(controllerClass)));
        }
    }

    private HandlerKey createHandlerKeyFrom(Method declaredMethod) {
        RequestMapping requestMapping = declaredMethod.getAnnotation(RequestMapping.class);
        return new HandlerKey(requestMapping.value(), requestMapping.method());
    }

    private Object createHandler(Class<?> controllerClass) {
        try {
            Constructor<?> constructor = controllerClass.getConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private void inputHandlerKeyAndHandler(HandlerKey handlerKey,
                                           HandlerExecution handlerExecution
    ) {
        if (handlerExecutions.containsKey(handlerKey)) {
            throw new IllegalStateException(String
                .format(
                    "해당 path 와 method엔 이미 핸들러가 맵핑되어 있습니다. 맵핑 정보 : %s", handlerKey)
            );
        }
        handlerExecutions.put(handlerKey, handlerExecution);
    }

    public Object getHandler(HttpServletRequest request) {
        return handlerExecutions.get(new HandlerKey(request));
    }
}
