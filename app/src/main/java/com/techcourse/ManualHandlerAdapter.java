package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ManualHandlerAdapter implements HandlerAdapter {

    private final ManualHandlerMapping handlerMapping;

    public ManualHandlerAdapter(ManualHandlerMapping handlerMapping) {
        this.handlerMapping = handlerMapping;
    }

    @Override
    public boolean isSupports(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        Controller handler = handlerMapping.getHandler(requestURI);
        return handler != null;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        Controller controller = handlerMapping.getHandler(request.getRequestURI());
        String viewName = controller.execute(request, response);
        return new ModelAndView(new JspView(viewName));
    }
}
