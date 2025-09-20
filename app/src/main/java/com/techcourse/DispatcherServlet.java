package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
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

    private HandlerAdapter handlerAdapter;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        handlerAdapter = new HandlerAdapter(
                List.of(
                        new AnnotationHandlerMapping("com.techcourse")
                )
        );
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            ModelAndView modelAndView = executeHandler(request, response);
            final View view = modelAndView.getView();
            view.render(modelAndView.getModel(), request, response);
        } catch (Exception e) {
            log.error("Exception for [{}] request at '{}' : {}", request.getMethod(), request.getRequestURI(), e.getMessage(), e);
            throw new ServletException(e.getMessage(), e);
        }
    }

    private ModelAndView executeHandler(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        HandlerExecution handler = handlerAdapter.findHandler(request);
        if (handler == null) {
            throw new IllegalArgumentException("No handler to handle the request");
        }
        return handler.handle(request, response);
    }
}
