package nextstep.mvc.controller.tobe;

import nextstep.mvc.exeption.HandlerAdapterException;
import nextstep.mvc.mapping.AnnotationHandlerMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class HandlerExecution {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object handler;
    private final Method method;

    public HandlerExecution(Object handler, Method method) {
        this.handler = handler;
        this.method = method;
    }

    public Object getHandler() {
        return handler;
    }

    public Object handle(Object... parameters) {
        try {
            final Class<?>[] parameterTypes = method.getParameterTypes();
            final Object[] argument = new Object[parameterTypes.length];

            for (int idx = 0; idx < parameterTypes.length; idx++) {
                final int finalIdx = idx;
                Arrays.stream(parameters)
                        .filter(it -> isAssignableFrom(parameterTypes[finalIdx], it))
                        .findAny()
                        .ifPresent(it -> argument[finalIdx] = it);
            }
            return method.invoke(handler, argument);
        } catch (Exception e) {
            log.info("핸들러 실행을 실패했습니다. 이유: {}", ((InvocationTargetException) e).getTargetException().getMessage());
            throw new HandlerAdapterException("핸들러 실행을 실패했습니다. 이유: " + ((InvocationTargetException) e).getTargetException().getMessage());
        }
    }

    private boolean isAssignableFrom(Class<?> parameterType, Object param) {
        return parameterType.isAssignableFrom(param.getClass());
    }
}
