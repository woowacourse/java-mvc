package com.interface21.webmvc.servlet.mvc.asis;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.HandlerAdapter;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerHandlerAdapter implements HandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(ControllerHandlerAdapter.class);

    @Override
    public boolean supports(final Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) 
            throws Exception {
        validateHandler(handler);
        
        final Controller controller = (Controller) handler;
        log.debug("Executing controller: {}", controller.getClass().getSimpleName());
        
        final String viewName = controller.execute(request, response);
        
        return createModelAndView(viewName);
    }

    private void validateHandler(final Object handler) {
        if (!supports(handler)) {
            throw new IllegalArgumentException(
                String.format("Handler of type %s is not supported by ControllerHandlerAdapter", 
                    handler.getClass().getName()));
        }
    }

    private ModelAndView createModelAndView(final String viewName) {
        if (viewName == null) {
            throw new IllegalStateException("Controller returned null viewName");
        }
        
        final JspView view = new JspView(viewName);
        return new ModelAndView(view);
    }
}
