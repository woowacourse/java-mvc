package com.techcourse;

import com.techcourse.support.web.adapter.HandlerAdapters;
import com.techcourse.support.web.mapping.HandlerMappings;
import com.techcourse.support.web.resolver.ViewResolvers;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.View;
import webmvc.org.springframework.web.servlet.mvc.HandlerAdapter;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final transient ViewResolvers resolver = new ViewResolvers();
    private final transient HandlerMappings mapping = new HandlerMappings();
    private final transient HandlerAdapters adapter = new HandlerAdapters();

    @Override
    public void init() {
        resolver.initialize();
        mapping.initialize();
        adapter.initialize(resolver);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final Object handler = mapping.getHandler(request);

            if (handler == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return ;
            }

            final HandlerAdapter handlerAdapter = adapter.getHandlerAdapter(handler);
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
