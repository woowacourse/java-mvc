package com.techcourse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.asis.ControllerHandlerAdaptor;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecutionHandlerAdaptor;
import webmvc.org.springframework.web.servlet.view.JspView;
import webmvc.org.springframework.web.servlet.view.ViewAdapter;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegistry handlerMapping;
    private final HandlerAdapterRegistry handlerAdapter;

    public DispatcherServlet() {
        this(new HandlerMappingRegistry(Set.of(
                        new ManualHandlerMapping(),
                        new AnnotationHandlerMapping("com"))
                ),
                new HandlerAdapterRegistry(Set.of(
                        new ControllerHandlerAdaptor(new ViewAdapter()),
                        new HandlerExecutionHandlerAdaptor())
                )
        );
    }

    public DispatcherServlet(HandlerMappingRegistry handlerMapping, HandlerAdapterRegistry handlerAdapter) {
        this.handlerMapping = handlerMapping;
        this.handlerAdapter = handlerAdapter;
    }

    @Override
    public void init() {
        handlerMapping.initialize();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        String requestURI = request.getRequestURI();
        String requestMethod = request.getMethod();
        log.debug("Method : {}, Request URI : {}", requestMethod, requestURI);

        try {
            Optional<Object> optionalHandler = handlerMapping.getHandler(request);
            if (optionalHandler.isEmpty()) {
                response.sendRedirect("404.jsp");
                return;
            }
            Object handler = optionalHandler.get();
            HandlerAdapter handlerAdapter = this.handlerAdapter.getHandlerAdapter(handler);
            ModelAndView modelAndView = handlerAdapter.handle(handler, request, response);

            // TODO: 3단계 View 적용
        } catch (Exception e) {
            log.debug(e.getMessage());
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
