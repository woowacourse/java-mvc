package com.interface21.webmvc.servlet;

import com.interface21.webmvc.servlet.handler.HandlerAdapter;
import com.interface21.webmvc.servlet.handler.HandlerReturnValueHandler;
import com.interface21.webmvc.servlet.handler.mapping.HandlerMappings;
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
        try {
            final String requestURI = httpServletRequest.getRequestURI();
            log.debug("Method : {}, Request URI : {}", httpServletRequest.getMethod(), requestURI);

            Object handler = this.handlerMappings.getHandler(httpServletRequest);
            Object handlerResult = HandlerAdapter.executeHandler(handler, httpServletRequest, httpServletResponse);
            HandlerReturnValueHandler.handle(handlerResult, httpServletRequest, httpServletResponse);
        } catch (Exception exception) {
            throw new ServletException("알 수 없는 오류가 발생하였습니다.", exception);
        }
    }
}
