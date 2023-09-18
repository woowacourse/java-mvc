package com.techcourse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.HandlerAdapters;
import nextstep.mvc.HandlerMapping;
import nextstep.mvc.HandlerMappings;
import nextstep.mvc.controller.adapter.ControllerHandlerAdapter;
import nextstep.mvc.controller.adapter.HandlerExecutionHandlerAdapter;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.view.JspView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappings handlerMappings;
    private final HandlerAdapters handlerAdapters;

    public DispatcherServlet(final HandlerMappings handlerMappings, final HandlerAdapters handlerAdapters) {
        this.handlerMappings = handlerMappings;
        this.handlerAdapters = handlerAdapters;
    }

    public DispatcherServlet() {
        this(new HandlerMappings(), new HandlerAdapters());
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        this.handlerMappings.addHandlerMapping(handlerMapping);
    }

    @Override
    public void init() {
        handlerMappings.addHandlerMapping(new AnnotationHandlerMapping("com.techcourse"));
        handlerMappings.addHandlerMapping(new ManualHandlerMapping());
        log.info("INITIALIZED HANDLER MAPPPINGS!");

        handlerAdapters.addHandlerAdapter(new HandlerExecutionHandlerAdapter());
        handlerAdapters.addHandlerAdapter(new ControllerHandlerAdapter());
        log.info("INITIALIZED HANDLER ADPATERS!");
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            final var controller = (Controller) handlerMappings.findHandlerMapping(request).get();
            final var viewName = controller.execute(request, response);
            move(viewName, request, response);
        } catch (Throwable e) {
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
