package com.techcourse;

import com.interface21.webmvc.servlet.HandlerAdapter;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);
    private HandlerMappingRegistry handlerMappingRegistry;
    private HandlerAdapterRegistry handlerAdapterRegistry;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        handlerMappingRegistry.addHandlerMapping(new ManualHandlerMapping());
        handlerMappingRegistry.addHandlerMapping(new AnnotationHandlerMapping());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        Object handler = handlerMappingRegistry.getHandler(request)
                .orElseThrow(IllegalStateException::new);
        HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);
        try {
            ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);
            View view = modelAndView.getView();
            Map<String, Object> model = modelAndView.getModel();
            view.render(model, request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
