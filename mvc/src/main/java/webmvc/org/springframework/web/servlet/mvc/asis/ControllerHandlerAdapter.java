package webmvc.org.springframework.web.servlet.mvc.asis;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.HandlerAdapter;
import webmvc.org.springframework.web.servlet.view.JspView;

public class ControllerHandlerAdapter implements HandlerAdapter {
    @Override
    public boolean isSupport(final Object object) {
        return object instanceof Controller;
    }

    @Override
    public ModelAndView handle(final Object object, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final Controller controller = (Controller) object;
        final String path = controller.execute(request, response);

        return new ModelAndView(new JspView(path));
    }
}
