package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMappingRegistry;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegistry handlerMappingRegistry;

    public DispatcherServlet() {
        this.handlerMappingRegistry = new HandlerMappingRegistry();
    }

    @Override
    public void init() {
        final ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        final AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping();
        manualHandlerMapping.initialize();
        annotationHandlerMapping.initialize();
        handlerMappingRegistry.addHandlerMapping(manualHandlerMapping);
        handlerMappingRegistry.addHandlerMapping(annotationHandlerMapping);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        final Optional<Object> handlerOptional = handlerMappingRegistry.getHandler(request);
        if (handlerOptional.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        final Object handler = handlerOptional.get();

        try {
            if (handler instanceof Controller) {
                final String execute = ((Controller) handler).execute(request, response);
                final var view = new JspView(execute);
                view.render(buildModel(request), request, response);
            } else if (handler instanceof HandlerExecution) {
                final ModelAndView modelAndView = ((HandlerExecution) handler).handle(request, response);
                modelAndView.getView().render(modelAndView.getModel(), request, response);
            } else {
                throw new IllegalArgumentException("Unknown handler type: " + handler.getClass());
            }
        } catch (final Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private HashMap<String, Object> buildModel(final HttpServletRequest request) {
        final var model = new HashMap<String, Object>();
        final Enumeration<String> attributeNames = request.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            final String attributeName = attributeNames.nextElement();
            final Object attributeValue = request.getAttribute(attributeName);
            model.put(attributeName, attributeValue);
        }
        return model;
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMappingRegistry.addHandlerMapping(handlerMapping);
    }
}
