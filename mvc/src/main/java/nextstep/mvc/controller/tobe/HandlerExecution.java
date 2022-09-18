package nextstep.mvc.controller.tobe;

import java.lang.reflect.Method;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.view.ModelAndView;

public class HandlerExecution implements Controller {

    private final Object controller;
    private final Method method;

    public HandlerExecution(Object controller, Method method) {
        this.controller = controller;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return (ModelAndView)method.invoke(controller, request, response);
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        handle(req, res);
        return null;
    }
}
