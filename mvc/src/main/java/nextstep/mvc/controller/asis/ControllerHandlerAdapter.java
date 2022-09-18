package nextstep.mvc.controller.asis;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;

public class ControllerHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        final String viewName = ((Controller) handler).execute(request, response);
        return move(viewName);
    }

    private ModelAndView move(final String viewName) {
        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            return new ModelAndView(new JspView(viewName.substring(JspView.REDIRECT_PREFIX.length())));
        }
        return new ModelAndView(new JspView(viewName));
    }
}
