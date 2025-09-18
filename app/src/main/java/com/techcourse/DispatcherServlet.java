package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerMappings handlerMappings;
    private HandlerAdapters handlerAdapters;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        this.handlerMappings = new HandlerMappings(List.of(new ManualHandlerMapping(), new AnnotationHandlerMapping()));
        this.handlerAdapters = new HandlerAdapters(List.of(new ControllerHandlerAdapter(), new HandlerExecutionAdapter()));
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final Object handler = handlerMappings.getHandler(request);
            if (handler == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            final HandlerAdapter adapter = handlerAdapters.getHandlerAdapter(handler);
            final ModelAndView modelAndView = adapter.handle(request, response, handler);
            modelAndView.render(request, response);

        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
