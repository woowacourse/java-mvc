package com.techcourse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.asis.ControllerHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.asis.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.asis.HandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecutionHandlerAdapter;
import webmvc.org.springframework.web.servlet.view.JspView;

import java.util.List;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerAdapterRegistry handlerAdapterRegistry;
    private HandlerMappingRegistry handlerMappingRegistry;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        final List<HandlerMapping> handlerMappings = List.of(
                new ManualHandlerMapping(),
                new AnnotationHandlerMapping("com.techcourse")
        );
        handlerMappingRegistry = new HandlerMappingRegistry(handlerMappings);
        final List<HandlerAdapter> handlerAdapters = List.of(
                new ControllerHandlerAdapter(),
                new HandlerExecutionHandlerAdapter()
        );
        handlerAdapterRegistry = new HandlerAdapterRegistry(handlerAdapters);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final HandlerMapping handlerMapping = handlerMappingRegistry.getHandlerMapping(request);
            final HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(request, handlerMapping);
            final ModelAndView view = handlerAdapter.handle(handlerMapping, request, response);
            move(view, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void move(final ModelAndView view, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        // TODO : 3단계에서 리팩토링 예정
        final String name = view.getView().getClass().getName();
        moveString(name, request, response);
    }

    private void moveString(final String view, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        if (view.startsWith(JspView.REDIRECT_PREFIX)) {
            response.sendRedirect(view.substring(JspView.REDIRECT_PREFIX.length()));
            return;
        }

        final var requestDispatcher = request.getRequestDispatcher(view);
        requestDispatcher.forward(request, response);
    }
}
