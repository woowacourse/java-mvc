package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import nextstep.mvc.view.ModelAndView;

public class HandlerExecution {

    private final Method method;
    private final Object controller;

    public HandlerExecution(Method method) {
        this.method = method;
        this.controller = getController(method);
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(controller, request, response);
    }

    private Object getController(Method method) {
        try {
            return method.getDeclaringClass().getConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("Method로부터 Controller를 찾을 수 없습니다.");
        }
    }
}
