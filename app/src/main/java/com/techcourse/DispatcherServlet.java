package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerMappingRegistry handlermappingRegistry;

    public DispatcherServlet() {
        this.handlermappingRegistry = new HandlerMappingRegistry();
    }

    @Override
    public void init() {
        var manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();
        handlermappingRegistry.adHandlerMapping(manualHandlerMapping);

        var annotationHandlerMapping = new AnnotationHandlerMapping();
        annotationHandlerMapping.initialize();
        handlermappingRegistry.adHandlerMapping(annotationHandlerMapping);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        final var handler = handlermappingRegistry.getHandler(request)
                .orElseThrow(() -> new ServletException("No handler found for request URI : " + requestURI));

        try {
            if (handler instanceof Controller) {
                final var viewName = ((Controller) handler).execute(request, response);
                move(viewName, request, response);
            } else if (handler instanceof HandlerExecution) {
                final var mav = ((HandlerExecution) handler).handle(request, response);
                render(mav, request, response);
            } else {
                // throw exception
            }
        } catch (final Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void render(final ModelAndView mav, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        var view = mav.getView();
        var model = mav.getModel();
        view.render(model, request, response);
    }

    private void move(final String viewName, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
            return;
        }

        final var requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }
}
