package nextstep.mvc.controller.tobe;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import nextstep.mvc.controller.MethodParameter;

public class MethodHandler {

    private final Method method;
    private final Object target;
    private final List<? extends Annotation> annotations;
    private final List<MethodParameter> methodParameters;

    public MethodHandler(Method method, Object target) {
        this.method = method;
        this.target = target;
        this.annotations = Arrays.asList(method.getAnnotations());
        this.methodParameters = methodParameters();
    }

    private List<MethodParameter> methodParameters() {
        List<MethodParameter> methodParameters = new ArrayList<>();
        final Class<?>[] parameterTypes = method.getParameterTypes();
        final Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameterTypes.length; i++) {
            methodParameters.add(new MethodParameter(parameterTypes[i], parameters[i], i));
        }
        return methodParameters;
    }

    public Object invoke(Object ... args) {
        try {
            return method.invoke(target, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("no supported method");
        }
    }

    public int getMethodParameterSize() {
        return methodParameters.size();
    }

    public List<MethodParameter> getMethodParameters() {
        return methodParameters;
    }

    public boolean isAnnotationPresents(Class<? extends Annotation> responseClass) {
        return annotations.stream().anyMatch(annotation -> annotation.getClass().isAssignableFrom(responseClass));
    }

    public boolean sameReturnTypeWith(Class<?> returnClass) {
        return method.getReturnType().isAssignableFrom(returnClass);
    }
}
