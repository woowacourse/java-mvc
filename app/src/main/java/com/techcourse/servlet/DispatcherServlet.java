package com.techcourse.servlet;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.techcourse.servlet.handler.HandlerMappings;
import com.techcourse.servlet.handler.adapter.HandlerAdapter;
import com.techcourse.servlet.view.ViewResolver;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final HandlerMappings DEFAULT_HANDLER_MAPPINGS = HandlerMappings.defaultHandlerMappings();

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappings handlerMappings;
    private final HandlerAdapter handlerAdapter;
    private final ViewResolver viewResolver;

    public DispatcherServlet() {
        this(DEFAULT_HANDLER_MAPPINGS);
    }

    public DispatcherServlet(HandlerMappings handlerMappings) {
        this.handlerMappings = handlerMappings;
        this.handlerAdapter = new HandlerAdapter();
        this.viewResolver = new ViewResolver();
    }

    @Override
    public void init() {
        handlerMappings.initialize();
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            Object handler = handlerMappings.getHandler(request);
            ModelAndView modelAndView = handlerAdapter.adaptHandler(handler, request, response);
            modelAndView.render(request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            renderViewByStatusCode(request, response, 500);
            throw new ServletException(e.getMessage());
        }
    }

    private void renderViewByStatusCode(HttpServletRequest request, HttpServletResponse response, int statusCode) {
        View view = viewResolver.resolveByStatusCode(statusCode);
        ModelAndView modelAndView = new ModelAndView(view);
        modelAndView.render(request, response);
    }
}
