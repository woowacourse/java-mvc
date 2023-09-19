package webmvc.org.springframework.web.servlet.mvc.tobe.handleradapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.view.JspView;

public class ControllerHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean isSupport(final Object handler) {
        System.out.println("b");
        System.out.println(handler.getClass());
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request,
                               final HttpServletResponse response,
                               final Object handler) throws Exception {
        System.out.println("=====b======");
        final Controller controller = (Controller) handler;
        final String viewPath = controller.execute(request, response);
        return new ModelAndView(new JspView(viewPath));
    }
}
