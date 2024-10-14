package com.interface21.webmvc.servlet.mvc.framework;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.CantRenderException;
import com.interface21.webmvc.servlet.mvc.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.HandlerMappingAdapter;
import com.interface21.webmvc.servlet.mvc.NoMatchedHandlerException;
import com.interface21.webmvc.servlet.mvc.annotation.AnnotationHandlerMappingAdapter;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        handlerMappingAdapters = Stream.of(new AnnotationHandlerMappingAdapter(basePackage))
                .peek(AnnotationHandlerMappingAdapter::initialize)
                .map(adapter -> (HandlerMappingAdapter) adapter)
                .toList();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            HandlerAdapter handlerAdapter = getHandlerAdapter(request);
            ModelAndView modelAndView = handlerAdapter.handle(request, response);
            modelAndView.getView().render(modelAndView.getModel(), request, response);
        } catch (NoMatchedHandlerException e) {
            renderToErrorPage(request, response);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage(), e);
        }
    }

    private void renderToErrorPage(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setStatus(404);
            new JspView("/404.jsp").render(new HashMap<>(), request, response);
        } catch (Exception e) {
            throw new CantRenderException(e);
        }
    }

    private HandlerAdapter getHandlerAdapter(HttpServletRequest request) {
        return handlerMappingAdapters.stream()
                .map(handlerMappingAdapter -> handlerMappingAdapter.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new NoMatchedHandlerException(request));
    }
}
