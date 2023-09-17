package com.techcourse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.View;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;
import webmvc.org.springframework.web.servlet.view.JspView;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final transient List<HandlerMapping> handlerMappings;

    public static class Builder {

        private final List<HandlerMapping> handlerMappings;

        public Builder() {
            this.handlerMappings = new ArrayList<>();
        }

        public Builder addHandlerMapping(final HandlerMapping handlerMapping) {
            handlerMappings.add(handlerMapping);
            return this;
        }

        public DispatcherServlet build() {
            //todo: handlerMapping이 암것도 안들어온다면?
            return new DispatcherServlet(this);
        }

        public List<HandlerMapping> getHandlerMappings() {
            return handlerMappings;
        }
    }

    private DispatcherServlet(final Builder builder) {
        this.handlerMappings = builder.getHandlerMappings();
    }

    @Override
    public void init() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final var handler = findHandler(request);
            handle(handler, request, response);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object findHandler(final HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findAny()
                .orElseThrow();
    }

    private void handle(final Object handler, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        // todo: 리팩터링
        if (handler instanceof Controller) {
            final String viewName = ((Controller) handler).execute(request, response);
            move(viewName, request, response);
            return;
        }
        if(handler instanceof HandlerExecution) {
            final ModelAndView modelAndView = ((HandlerExecution) handler).handle(request, response);
            final View view = modelAndView.getView();
            final Map<String, Object> model = modelAndView.getModel();
            view.render(model, request, response);
            return;
        }
        throw new UnsupportedOperationException();
    }

    private void move(final String viewName, final HttpServletRequest request, final HttpServletResponse response)
            throws IOException, ServletException {
        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
            return;
        }

        final var requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }
}
