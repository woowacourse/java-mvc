package com.interface21.webmvc.servlet.mvc;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.handleradaptor.AnnotationHandlerAdaptor;
import com.interface21.webmvc.servlet.mvc.handleradaptor.HandlerAdaptor;
import com.interface21.webmvc.servlet.mvc.handlermapping.AnnotationHandlerMapping;
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

    public DispatcherServlet() {
        this.handlerMappingRegistry = new HandlerMappingRegistry();
        this.handlerAdaptorRegistry = new HandlerAdaptorRegistry();
    }

    @Override
    public void init() {
        // init handlerMapping
        final var annotationHandlerMapping = new AnnotationHandlerMapping("com.techcourse");
        annotationHandlerMapping.initialize();
        handlerMappingRegistry.addHandlerMapping(annotationHandlerMapping);

        // init handlerAdaptor
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
            modelAndView.render(request, response);
        } catch (Exception e) {
            throw new ServletException(e.getMessage());
        }
    }
}
