package com.interface21.webmvc.servlet.mvc;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.NoSuchHandlerException;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapters;
import com.interface21.webmvc.servlet.mvc.tobe.RequestHandlerMappings;
import com.interface21.webmvc.servlet.view.JspView;
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
    public static final String NOT_FOUND_VIEW_NAME = "/404.jsp";

    private RequestHandlerMappings requestHandlerMappings;
    private HandlerAdapters handlerAdapters;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        requestHandlerMappings = new RequestHandlerMappings();
        handlerAdapters = new HandlerAdapters();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            Object handler = requestHandlerMappings.getHandler(request);
            ModelAndView modelAndView = handlerAdapters.handle(handler, request, response);
            View view = modelAndView.getView();
            view.render(modelAndView.getModel(), request, response);
        } catch (NoSuchHandlerException e) {
            execute404(request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void execute404(HttpServletRequest request, HttpServletResponse response) {
        View view = new JspView(NOT_FOUND_VIEW_NAME);
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        try {
            view.render(Map.of(), request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
