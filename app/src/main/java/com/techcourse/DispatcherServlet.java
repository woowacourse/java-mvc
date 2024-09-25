package com.techcourse;

import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.tobe.adapter.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.adapter.HandlerAdapters;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final RequestHandlerMapping requestHandlerMapping;
    private final HandlerAdapters handlerAdapters;

    public DispatcherServlet() {
        this.requestHandlerMapping = new RequestHandlerMapping();
        this.handlerAdapters = new HandlerAdapters();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final Object handler = requestHandlerMapping.getHandler(request);
            if (handler == null) {
                sendNotFoundResponse();
                return;
            }

            final HandlerAdapter handlerAdapter = handlerAdapters.getHandlerAdapter(handler);
            final ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);
            final View view = modelAndView.getView();
            final Map<String, Object> model = modelAndView.getModel();
            view.render(model, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void sendNotFoundResponse() {
        log.info("Not Found Response");
    }
}
