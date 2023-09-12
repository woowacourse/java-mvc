package com.techcourse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.View;
import webmvc.org.springframework.web.servlet.mvc.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.HandlerAdapterRegistry;
import webmvc.org.springframework.web.servlet.mvc.HandlerMappingRegistry;
import webmvc.org.springframework.web.servlet.mvc.asis.ControllerHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecutionHandlerAdapter;
import webmvc.org.springframework.web.servlet.view.JspView;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
    private final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        handlerMappingRegistry.addHandlerMapping(new ManualHandlerMapping());
        handlerMappingRegistry.addHandlerMapping(new AnnotationHandlerMapping());
        handlerAdapterRegistry.addHandlerAdapter(new HandlerExecutionHandlerAdapter());
        handlerAdapterRegistry.addHandlerAdapter(new ControllerHandlerAdapter());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final Optional<Object> handler = handlerMappingRegistry.getHandler(request);
            if (handler.isEmpty()) {
                response.setStatus(404);
                return;
            }
            final Object getHandlers = handler.get();
            final HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(getHandlers);
            final ModelAndView modelAndView = handlerAdapter.handle(request, response, getHandlers);
            render(modelAndView, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void render(
            final ModelAndView modelAndView,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {
        final View view = modelAndView.getView();
        final String viewName = view.getViewName();
        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
            return;
        }

        final var requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
//        3 단계
//        final View view = modelAndView.getView();
//        view.render(modelAndView.getModel(), request, response);
    }
}
