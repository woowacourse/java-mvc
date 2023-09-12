package webmvc.org.springframework.web.servlet.mvc.asis;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.HandlerAdaptor;
import webmvc.org.springframework.web.servlet.view.JspView;

public class ControllerHandlerAdaptor implements HandlerAdaptor {

    private final Controller controller;

    public ControllerHandlerAdaptor(final Controller controller) {
        this.controller = controller;
    }

    public boolean supports(Object target) {
        try {
            final Method method = target.getClass()
                    .getDeclaredMethod("execute", HttpServletRequest.class, HttpServletResponse.class);
            return method.getReturnType().isAssignableFrom(String.class);
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final String path = controller.execute(request, response);
        return new ModelAndView(new JspView(path));
    }
}
