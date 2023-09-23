package webmvc.org.springframework.web.servlet.mvc.tobe.handleradapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.mvc.tobe.exception.InvalidHandlerForHandlerAdapterException;
import webmvc.org.springframework.web.servlet.view.JspView;

public class ControllerHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean isHandleable(final Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(final Object handler, final HttpServletRequest request,
        final HttpServletResponse response) throws Exception {
        if (!isHandleable(handler)) {
            throw new InvalidHandlerForHandlerAdapterException(handler, this);
        }
        final String viewName = ((Controller) handler).execute(request, response);
        final JspView jspView = new JspView(viewName);
        return new ModelAndView(jspView);
    }
}
