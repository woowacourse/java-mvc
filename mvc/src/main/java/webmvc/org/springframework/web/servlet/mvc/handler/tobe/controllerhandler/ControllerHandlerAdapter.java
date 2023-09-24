package webmvc.org.springframework.web.servlet.mvc.handler.tobe.controllerhandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.frontcontroller.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.handler.asis.Controller;
import webmvc.org.springframework.web.servlet.view.JspView;

public class ControllerHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean isSupporting(final Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request,
                               final HttpServletResponse response,
                               final Object handler) throws Exception {
        final Controller controller = (Controller) handler;
        final String viewPath = controller.execute(request, response);
        return new ModelAndView(new JspView(viewPath));
    }
}
