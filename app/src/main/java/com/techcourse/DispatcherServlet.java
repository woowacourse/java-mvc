package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerAdaptor;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdaptor;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdaptorRegistry;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMappingRegistry;
import com.interface21.webmvc.servlet.mvc.tobe.ManualHandlerAdaptor;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegistry handlerMappingRegistry;
    private final HandlerAdaptorRegistry handlerAdaptorRegistry;

    public DispatcherServlet(final HandlerMappingRegistry handlerMappingRegistry,
                             final HandlerAdaptorRegistry handlerAdaptorRegistry) {
        this.handlerMappingRegistry = handlerMappingRegistry;
        this.handlerAdaptorRegistry = handlerAdaptorRegistry;
    }

    @Override
    public void init() {
        // init handlerMapping
        final var manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();
        handlerMappingRegistry.addHandlerMapping(manualHandlerMapping);
        final var annotationHandlerMapping = new AnnotationHandlerMapping("com.techcourse");
        annotationHandlerMapping.initialize();
        handlerMappingRegistry.addHandlerMapping(annotationHandlerMapping);

        // init handlerAdaptor
        handlerAdaptorRegistry.addHandlerAdaptor(new ManualHandlerAdaptor());
        handlerAdaptorRegistry.addHandlerAdaptor(new AnnotationHandlerAdaptor());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final Optional<Object> handlerOptional = handlerMappingRegistry.getHandler(request);
            if (handlerOptional.isEmpty()) {
                throw new IllegalStateException("지원하지 않는 요청입니다. :" + requestURI);
            }
            final Object handler = handlerOptional.get();
            final HandlerAdaptor handlerAdaptor = handlerAdaptorRegistry.getHandlerAdaptor(handler);
            final ModelAndView modelAndView = handlerAdaptor.handle(handler, request, response);
            final View view = modelAndView.getView();
            view.render(modelAndView.getModel(), request, response);
        } catch (Exception e) {
            throw new ServletException(e.getMessage());
        }
    }
}
