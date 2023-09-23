package webmvc.org.springframework.web.servlet.mvc.asis;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.exception.HandlerAdapterException;
import webmvc.org.springframework.web.servlet.view.JspView;

public class ControllerHandlerAdapter implements HandlerAdapter {

    private static final String HANDLER_CLASS_NAME = ControllerHandlerAdapter.class.getCanonicalName();

    @Override
    public boolean supports(Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        validateHandlerType(handler);
        final var controller = (Controller) handler;
        final var viewName = controller.execute(request, response);

        return new ModelAndView(new JspView(viewName));
    }

    private void validateHandlerType(Object handler) {
        if (!supports(handler)) {
            throw new HandlerAdapterException("unsupported handler adaptor for " + HANDLER_CLASS_NAME);
        }
    }

}
