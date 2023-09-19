package com.techcourse;

import com.techcourse.support.web.mapping.HandlerMappings;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.View;
import webmvc.org.springframework.web.servlet.mvc.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.adapter.HandlerAdapters;
import webmvc.org.springframework.web.servlet.mvc.view.resolver.ViewResolvers;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final transient ViewResolvers viewResolvers = new ViewResolvers();
    private final transient HandlerMappings mappings = new HandlerMappings();
    private final transient HandlerAdapters adapters = new HandlerAdapters();

    @Override
    public void init() {
        viewResolvers.initialize();
        mappings.initialize();
        adapters.initialize(viewResolvers);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final Object handler = mappings.getHandler(request);

            if (handler == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return ;
            }

            final HandlerAdapter handlerAdapter = adapters.getHandlerAdapter(handler);
            final ModelAndView modelAndView = handlerAdapter.execute(request, response, handler);

            move(modelAndView, request, response);
        } catch (final Exception e) {
            log.error("Exception : {}", e.getMessage(), e);

            throw new ServletException(e.getMessage());
        }
    }

    private void move(
            final ModelAndView modelAndView,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {
        final View view = modelAndView.getView();

        if (view == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return ;
        }

        view.render(modelAndView.getModel(), request, response);
    }
}
