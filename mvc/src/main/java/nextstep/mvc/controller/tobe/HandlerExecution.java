package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import nextstep.mvc.view.ModelAndView;

public class HandlerExecution {

    private final Method method;

    public HandlerExecution(final Method method) {
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final Object controller = findController();
        return (ModelAndView) method.invoke(controller, request, response);
    }

    private Object findController()
            throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        return Arrays.stream(method.getDeclaringClass()
                        .getConstructors())
                .filter(it -> it.getParameterCount() == 0)
                .findFirst()
                .orElseThrow(() -> new NoSuchMethodException("기본 생성자가 존재하지 않습니다."))
                .newInstance();
    }
}
