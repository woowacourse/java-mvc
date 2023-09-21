package com.techcourse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;
import webmvc.org.springframework.web.servlet.view.JspView;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String BASE_PACKAGE_PATH = "com.techcourse";

    private final List<HandlerMapping> handlerMappings;
    private final List<HandlerAdapter> handlerAdapters;

    public DispatcherServlet() {
        handlerMappings = new ArrayList<>();
        handlerAdapters = new ArrayList<>();
    }

    @Override
    public void init() {
        initHandlerMappings();
        initHandlerAdapters();
    }

    private void initHandlerMappings() {
        final List<HandlerMapping> handlerMappingInstances = HandlerMappingFactory.getHandlerMappings(BASE_PACKAGE_PATH)
            .stream()
            .peek(HandlerMapping::initialize)
            .collect(Collectors.toList());
        handlerMappings.addAll(handlerMappingInstances);
    }

    private void initHandlerAdapters() {
        final List<HandlerAdapter> handlerAdapterInstances = HandlerAdapterFactory.getHandlerAdapters();
        handlerAdapters.addAll(handlerAdapterInstances);
    }


    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());
        try {
            process(request, response);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void process(final HttpServletRequest request, final HttpServletResponse response)
        throws Exception {
        final Object handler = getHandler(request);
        final HandlerAdapter handlerAdapter = getHandlerAdapter(handler);
        final ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);
        move(modelAndView, request, response);
    }

    private Object getHandler(final HttpServletRequest request) {
        return handlerMappings.stream()
            .filter(mapping -> mapping.getHandler(request) != null)
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException("해당하는 HandlerMapping이 없습니다."))
            .getHandler(request);
    }

    private HandlerAdapter getHandlerAdapter(final Object handler) {
        return handlerAdapters.stream()
            .filter(adapter -> adapter.supports(handler))
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException("해당하는 HandlerAdapter가 없습니다."));
    }

    private void move(
        final ModelAndView modelAndView,
        final HttpServletRequest request,
        final HttpServletResponse response
    ) throws Exception {
        final String viewName = modelAndView.getViewName();
        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
            return;
        }

        final var requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }
}
