package webmvc.org.springframework.web.servlet.mvc.handlerAdapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.view.JspView;

public class DefaultHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean support(Object handler) {
        return handler instanceof DefaultHandlerAdapter;
    }

    @Override
    public ModelAndView handle(Object handler, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Controller controller = (Controller) handler;
        String viewName = controller.execute(request, response);
        return new ModelAndView(new JspView(viewName));
    }
}
