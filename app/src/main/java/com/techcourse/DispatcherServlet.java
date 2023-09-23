package com.techcourse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerAdaptors handlerAdaptors;
    private HandlerMappings handlerMappings;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        handlerAdaptors = new HandlerAdaptors();
        handlerMappings = new HandlerMappings(this.getClass().getPackageName());
        handlerMappings.initialize();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final var handler = handlerMappings.getHandler(request);
            final var modelAndView = handlerAdaptors.execute(handler, request, response);
            move(modelAndView, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void move(final ModelAndView modelAndView, final HttpServletRequest request,
                      final HttpServletResponse response)
            throws Exception {
        if (Objects.isNull(modelAndView.getViewName())) {
            // TODO JsonView
            return;
        }
        if (modelAndView.getViewName().startsWith(JspView.REDIRECT_PREFIX)) {
            response.sendRedirect(modelAndView.getViewName().substring(JspView.REDIRECT_PREFIX.length()));
            return;
        }

        final var requestDispatcher = request.getRequestDispatcher(modelAndView.getViewName());
        requestDispatcher.forward(request, response);
    }
}
