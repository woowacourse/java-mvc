package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;

public class HandlerExecution {

    private final ControllerMethod controllerMethod;

    public HandlerExecution(final ControllerMethod controllerMethod) {
        this.controllerMethod = controllerMethod;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return controllerMethod.execute(request, response);
    }
}
