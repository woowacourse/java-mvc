package nextstep.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.RedirectView;

public class HandlerControllerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String viewName = ((Controller) handler).execute(request, response);
        return toModelAndView(viewName);
    }

    private ModelAndView toModelAndView(String viewName) {
        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            return new ModelAndView(new RedirectView(viewName));
        }
        return new ModelAndView(new JspView(viewName));
    }
}
