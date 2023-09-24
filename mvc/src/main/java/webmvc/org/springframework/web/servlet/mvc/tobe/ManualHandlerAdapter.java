package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.view.JspView;

public class ManualHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean isSupport(Object object) {
        return object instanceof Controller;
    }

    @Override
    public ModelAndView handle(
            Object object,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        try {
            Controller controller = (Controller) object;
            String viewName = controller.execute(request, response);
            return new ModelAndView(new JspView(viewName));
        } catch (Exception exception) {
            throw new IllegalArgumentException(exception);
        }
    }

}
