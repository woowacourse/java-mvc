package webmvc.org.springframework.web.servlet.mvc.asis;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.HandlerAdapter;
import webmvc.org.springframework.web.servlet.view.JspView;

public class ControllerHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object object) {
        return object instanceof Controller;
    }

    @Override
    public ModelAndView handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        Controller controller = (Controller) handler;
        String viewName = controller.execute(httpServletRequest, httpServletResponse);
        ModelAndView mav = new ModelAndView(new JspView(viewName));
        return mav;
    }
}
