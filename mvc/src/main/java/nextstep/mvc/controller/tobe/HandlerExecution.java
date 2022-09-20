package nextstep.mvc.controller.tobe;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;

public class HandlerExecution {

    private final Method method;

    public HandlerExecution(final Method method) {
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response)
            throws InvocationTargetException, InstantiationException, IllegalAccessException {

        final var controller = emptyConstructor(method).newInstance();
        return (ModelAndView) method.invoke(controller, request, response);
    }

    private Constructor<?> emptyConstructor(final Method method) {
        try {
            final var declaringClass = method.getDeclaringClass();
            return declaringClass.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new NoSuchMethodError("빈 생성자를 조회할 수 없습니다.");
        }
    }
}
