package com.techcourse;

import com.interface21.webmvc.servlet.handler.HandlerAdapter;
import com.interface21.webmvc.servlet.view.ModelAndView;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerMappings handlerMappings;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        handlerMappings = HandlerMappings.initialize();
    }

    @Override
    protected void service(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse) throws ServletException {
        final String requestURI = httpServletRequest.getRequestURI();
        log.debug("Method : {}, Request URI : {}", httpServletRequest.getMethod(), requestURI);

        Object handler = this.handlerMappings.getHandler(httpServletRequest);
        ModelAndView modelAndView = HandlerAdapter.executeHandler(handler, httpServletRequest, httpServletResponse);
        modelAndView.render(httpServletRequest, httpServletResponse);
    }
}
