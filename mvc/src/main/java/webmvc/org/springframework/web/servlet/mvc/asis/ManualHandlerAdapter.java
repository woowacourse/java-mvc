package webmvc.org.springframework.web.servlet.mvc.asis;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdapter;
import webmvc.org.springframework.web.servlet.view.JspView;

public class ManualHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(final Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView execute(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
            throws Exception {
        final Controller controller = (Controller) handler;
        final String viewName = controller.execute(request, response);

        return toModelAndView(viewName);
    }

    private ModelAndView toModelAndView(final String viewName) {
        final JspView jspView = new JspView(viewName);
        return new ModelAndView(jspView);
    }
}
