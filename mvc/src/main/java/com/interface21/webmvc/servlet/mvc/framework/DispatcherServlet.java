package com.interface21.webmvc.servlet.mvc.framework;

import java.util.List;
import java.util.Objects;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.HandlerMappingAdapter;
import com.interface21.webmvc.servlet.mvc.NoMatchedHandlerException;
import com.interface21.webmvc.servlet.mvc.annotation.AnnotationHandlerMappingAdapter;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final Object[] basePackage;

    private List<HandlerMappingAdapter> handlerMappingAdapters;

    public DispatcherServlet(Object... basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    public void init() {
        handlerMappingAdapters = List.of(
                new AnnotationHandlerMappingAdapter(basePackage)
        );
        for (HandlerMappingAdapter handlerMappingAdapter : handlerMappingAdapters) {
            handlerMappingAdapter.initialize();
        }
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            HandlerAdapter handlerAdapter = handlerMappingAdapters.stream()
                    .map(handlerMappingAdapter -> handlerMappingAdapter.getHandler(request))
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElseThrow(() -> new NoMatchedHandlerException(request));
            ModelAndView modelAndView = handlerAdapter.handle(request, response);
            modelAndView.getView().render(modelAndView.getModel(), request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage(), e);
        }
    }
}
