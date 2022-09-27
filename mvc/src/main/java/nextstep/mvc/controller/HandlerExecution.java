package nextstep.mvc.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import nextstep.mvc.view.ModelAndView;

public class HandlerExecution {

    private final Object declaredController;
    private final Method method;

    public HandlerExecution(final Object declaredController, final Method method) {
        this.declaredController = declaredController;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        return (ModelAndView) method.invoke(declaredController, req, res);
    }
}
