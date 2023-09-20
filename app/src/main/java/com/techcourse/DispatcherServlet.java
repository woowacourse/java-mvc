package com.techcourse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.View;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.RequestMappingHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.SimpleControllerHandlerAdapter;
import webmvc.org.springframework.web.servlet.view.JspView;

import java.util.ArrayList;
import java.util.List;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private List<HandlerMapping> handlerMappings;
    private List<HandlerAdapter> handlerAdapters;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        handlerMappings = new ArrayList<>();
        handlerMappings.add(new ManualHandlerMapping());
        handlerMappings.add(new AnnotationHandlerMapping("com.techcourse"));
        handlerMappings.forEach(HandlerMapping::initialize);

        handlerAdapters = new ArrayList<>();
        handlerAdapters.add(new SimpleControllerHandlerAdapter());
        handlerAdapters.add(new RequestMappingHandlerAdapter());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final Object handler = handlerMappings.stream()
                .map(mapping -> mapping.getHandler(request))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 요정입니다."));
        final HandlerAdapter handlerAdapter = handlerAdapters.stream()
                .filter(Adapter -> Adapter.supports(handler))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 요정입니다."));

        try {
            final ModelAndView handle = handlerAdapter.handle(request, response, handler);
            final View view = handle.getView();
            if (view instanceof JspView) {
                move(((JspView) view).getViewName(), request, response);
            }
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void move(final String viewName, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
            return;
        }

        final var requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }
}
