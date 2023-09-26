package webmvc.org.springframework.web.servlet.mvc.tobe.handler.adapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.view.JspView;

public class ManualHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean isSupported(final Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView execute(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        Controller controller = (Controller) handler;
        String view = controller.execute(request, response);
        JspView jspView = new JspView(view);
        return new ModelAndView(jspView);
    }
}
