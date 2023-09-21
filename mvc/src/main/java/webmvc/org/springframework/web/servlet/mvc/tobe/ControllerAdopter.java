package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.view.JspView;

public class ControllerAdopter implements HandlerAdopter{

    @Override
    public boolean isSupport(final Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(
            final Object handler,
            final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        final String viewName = ((Controller) handler).execute(request, response);
        return new ModelAndView(new JspView(viewName));
    }
}
