package webmvc.org.springframework.web.servlet.mvc.asis;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

public class ControllerHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(final Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(final HandlerMapping handlerMapping, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final Controller controller = (Controller) handlerMapping.getHandler(request);
        final String result = controller.execute(request, response);
        final JspView jspView = new JspView(result);
        return new ModelAndView(jspView);
    }
}
