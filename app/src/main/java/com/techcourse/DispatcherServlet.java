package com.techcourse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.tobe.Handler;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.NotFoundHandler;
import webmvc.org.springframework.web.servlet.view.JspView;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final List<HandlerMapping> handlerMappings;

    public DispatcherServlet(final HandlerMapping... handlerMappings) {
        this.handlerMappings = Arrays.stream(handlerMappings)
            .collect(Collectors.toList());
    }

    @Override
    public void init() {
        for (final HandlerMapping handlerMapping : handlerMappings) {
            handlerMapping.initialize();
        }
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final var handler = getHandler(request);
            final var modelAndView = handler.handle(request, response);
            move(modelAndView, request, response);
        } catch (final Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private Handler getHandler(final HttpServletRequest request) {
        return handlerMappings.stream()
            .map(handlerMapping -> handlerMapping.getHandler(request))
            .filter(Objects::nonNull)
            .findAny()
            .orElseGet(NotFoundHandler::new);
    }

    private void move(final ModelAndView modelAndView, final HttpServletRequest request,
                      final HttpServletResponse response) throws Exception {
        final String viewName = modelAndView.getView().getViewName();
        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
            return;
        }

        final var requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }
}
