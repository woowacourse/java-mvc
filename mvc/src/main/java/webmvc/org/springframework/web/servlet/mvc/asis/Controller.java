package webmvc.org.springframework.web.servlet.mvc.asis;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.tobe.Handler;
import webmvc.org.springframework.web.servlet.view.JspView;

public interface Controller extends Handler {

    String execute(final HttpServletRequest req, final HttpServletResponse res) throws Exception;

    @Override
    default ModelAndView handle(final HttpServletRequest request,
                                       final HttpServletResponse response) throws Exception {
        final String viewName = execute(request, response);
        return new ModelAndView(new JspView(viewName));
    }
}
