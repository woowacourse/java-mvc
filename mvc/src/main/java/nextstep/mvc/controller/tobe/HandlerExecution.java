package nextstep.mvc.controller.tobe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;

public class HandlerExecution {

    private final Object instance;
    private final Method method;

    public HandlerExecution(final Class<?> handler, final Method method) {
        this.instance = generateInstance(handler);
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        Object[] params = {request, response};
        return (ModelAndView)method.invoke(instance, params);
    }

    private Object generateInstance(final Class<?> controller) {
        try {
            return controller.getConstructor().newInstance();
        } catch (NoSuchMethodException |
                 InstantiationException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException("컨트롤러 생성 과정에서 예외가 발생했습니다.");
        }
    }
}
