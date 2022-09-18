package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.view.ModelAndView;

public class HandlerExecution implements Controller {

    private final Object controller;
    private final Method method;

    public HandlerExecution(final Object controller, final Method method) {
        this.controller = controller;
        this.method = method;
    }

    @Override
    public String execute(final HttpServletRequest req,
                          final HttpServletResponse res) throws Exception {
        return null;
    }

    public ModelAndView handle(final HttpServletRequest request,
                               final HttpServletResponse response) throws Exception {
        // Todo: 더 좋은 방법이 있을듯?
        return (ModelAndView) method.invoke(controller, request, response);
    }
}
