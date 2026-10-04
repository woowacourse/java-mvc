package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.HandlerAdaptor;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.asis.SimpleControllerHandlerAdaptor;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerAdaptor;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final List<HandlerMapping> handlerMappings;
    private final List<HandlerAdaptor> handlerAdaptors;

    public DispatcherServlet() {
        handlerMappings = new ArrayList<>();
        handlerAdaptors = new ArrayList<>();
    }

    @Override
    public void init() {
        ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();

        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("com.techcourse.controller");
        annotationHandlerMapping.initialize();

        handlerMappings.add(manualHandlerMapping);
        handlerMappings.add(annotationHandlerMapping);

        handlerAdaptors.add(new SimpleControllerHandlerAdaptor());
        handlerAdaptors.add(new AnnotationHandlerAdaptor());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final var handler = getHandler(request);
            final var handlerAdaptor = getHandlerAdaptor(handler);
            ModelAndView mav = handlerAdaptor.handle(request, response, handler);
            mav.getView().render(mav.getModel(), request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object getHandler(final HttpServletRequest request) {
        for (final HandlerMapping handlerMapping : handlerMappings) {
            try {
                Object handler = handlerMapping.getHandler(request);

                if (handler != null) {
                    return handler;
                }

            } catch (Exception e) {
                throw new NoSuchElementException("요청에 대응하는 핸들러가 없습니다.");
            }
        }
        return null;
    }

    private HandlerAdaptor getHandlerAdaptor(final Object handler) {
        for (final HandlerAdaptor adaptor : handlerAdaptors) {
            if (adaptor.supports(handler)) {
                return adaptor;
            }
        }
        throw new NoSuchElementException("핸들러에 대응하는 어댑터가 없습니다.");
    }
}
