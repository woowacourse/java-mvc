package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import nextstep.mvc.view.ModelAndView;


public class HandlerExecution {

    private final Object controller;
    private final Method handle;

    public HandlerExecution(final Object controller, final Method handle) {
        this.controller = controller;
        this.handle = handle;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return (ModelAndView) handle.invoke(controller, request, response);
    }
}
