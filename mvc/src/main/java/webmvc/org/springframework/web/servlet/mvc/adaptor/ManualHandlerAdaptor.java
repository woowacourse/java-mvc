package webmvc.org.springframework.web.servlet.mvc.adaptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.view.JspView;

public class ManualHandlerAdaptor implements HandlerAdaptor {

    @Override
    public boolean isHandle(Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView execute(Object handler, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Controller controller = (Controller) handler;
        return new ModelAndView(new JspView(controller.execute(request, response)));
    }
}
