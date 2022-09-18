package nextstep.mvc.controller.asis;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;

public class ManualHandlerAdaptor implements HandlerAdapter {

    @Override
    public boolean supports(final Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response,
        final Object handler) {
        final Controller controller = (Controller)handler;
        final String viewName = controller.execute(request, response);
        final ModelAndView modelAndView = new ModelAndView(new JspView(viewName));
        modelAndView.addObject("viewName", viewName);
        return modelAndView;
    }
}
