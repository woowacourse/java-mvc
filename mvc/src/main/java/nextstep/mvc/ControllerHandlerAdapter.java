package nextstep.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;

public class ControllerHandlerAdapter implements HandlerAdapter {
    public ControllerHandlerAdapter() {
    }

    @Override
    public boolean supports(final Object handler) {
        return Controller.class.isAssignableFrom(handler.getClass());
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response,
                               final Object handler)
            throws Exception {
        return handleController(request, response, handler);
    }

    ModelAndView handleController(final HttpServletRequest request, final HttpServletResponse response,
                                  final Object controller) throws Exception {
        final var viewName = ((Controller) controller).execute(request, response);
        return new ModelAndView(new JspView(viewName));
    }
}
