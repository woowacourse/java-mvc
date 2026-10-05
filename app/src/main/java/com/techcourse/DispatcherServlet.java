package com.techcourse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.interface21.webmvc.servlet.view.JspView;

import java.util.Collections;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final ManualHandlerMapping manualHandlerMapping;

    public DispatcherServlet() {
        this(new ManualHandlerMapping());
    }

    DispatcherServlet(final ManualHandlerMapping manualHandlerMapping) {
        this.manualHandlerMapping = manualHandlerMapping;
    }

    @Override
    public void init() {
        manualHandlerMapping.initialize();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final var controller = manualHandlerMapping.getHandler(requestURI);
            final var viewName = controller.execute(request, response);
            new JspView(viewName).render(Collections.emptyMap(), request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

}
