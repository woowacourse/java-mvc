package com.techcourse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String NOT_FOUND_VIEW = "404.jsp";

    private HandlerMappings handlerMappings;
    private HandlerExecutor handlerExecutor;

    public DispatcherServlet() {
    }

    public DispatcherServlet(HandlerMappings handlerMappings, HandlerExecutor handlerExecutor) {
        this.handlerMappings = handlerMappings;
        this.handlerExecutor = handlerExecutor;
    }

    @Override
    public void init() {
        handlerMappings = new HandlerMappings();
        handlerExecutor = new HandlerExecutor();
        handlerMappings.init();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            ModelAndView modelAndView = findModelAndView(request, response);

            modelAndView.render(request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private ModelAndView findModelAndView(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object handler = handlerMappings.getHandler(request);
        if (handler == null) {
            return new ModelAndView(new JspView(NOT_FOUND_VIEW));
        }
        return handlerExecutor.execute(request, response, handler);
    }
}
