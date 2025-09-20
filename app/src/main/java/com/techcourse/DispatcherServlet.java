package com.techcourse;

import com.interface21.webmvc.servlet.controller.Controller;
import com.interface21.webmvc.servlet.handler.HandlerExecution;
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

        Object handler = getHandler(httpServletRequest);
        ModelAndView modelAndView = executeHandler(handler, httpServletRequest, httpServletResponse);
        modelAndView.render(httpServletRequest, httpServletResponse);
    }

    private Object getHandler(HttpServletRequest httpServletRequest) throws ServletException {
        return this.handlerMappings.getHandler(httpServletRequest);
    }

    private ModelAndView executeHandler(Object handler, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException {
        try {
            if (handler instanceof Controller) {
                return ((Controller) handler).execute(httpServletRequest, httpServletResponse);
            } else if (handler instanceof HandlerExecution) {
                return ((HandlerExecution) handler).handle(httpServletRequest, httpServletResponse);
            } else {
                throw new ServletException("지원하지 않는 응답 방식입니다.");
            }
        } catch (Exception exception) {
            log.error("Exception : {}", exception.getMessage(), exception);
            throw new ServletException(exception.getMessage());
        }
    }
}
