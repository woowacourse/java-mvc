package com.interface21.webmvc.servlet;

import com.interface21.webmvc.servlet.mvc.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.asis.ManualHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.asis.ManualHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.viewResolver.JspViewResolver;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final List<HandlerMapping> handlerMappings;
    private final List<HandlerAdapter> handlerAdapters;
    private final List<ViewResolver> viewResolvers;

    public DispatcherServlet(String basePackage, String... others) {
        handlerMappings = List.of(
                new AnnotationHandlerMapping(basePackage, others),
                new ManualHandlerMapping(basePackage, others)
        );
        handlerAdapters = List.of(
                new AnnotationHandlerAdapter(),
                new ManualHandlerAdapter()
        );
        viewResolvers = List.of(
                new JspViewResolver()
        );
    }

    @Override
    public void init() {
        for (HandlerMapping handlerMapping : handlerMappings) {
            handlerMapping.initialize();
        }
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final Object foundHandler = findHandler(request, requestURI);
            final HandlerAdapter foundHandlerAdapter = findHandlerAdapter(foundHandler, requestURI);
            final ModelAndView modelAndView = foundHandlerAdapter.handle(request, response, foundHandler);
            render(modelAndView, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object findHandler(HttpServletRequest request, String requestURI) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No HandlerMapping Found For " + requestURI));
    }

    private HandlerAdapter findHandlerAdapter(Object foundHandler, String requestURI) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(foundHandler))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No HandlerAdapter Found For " + requestURI));
    }

    private void render(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object viewObject = modelAndView.getView();
        View view = extractView(viewObject);
        view.render(modelAndView.getModel(), request, response);
    }

    private View extractView(Object viewObject) {
        if (viewObject instanceof View view) {
            return view;
        } else if (viewObject instanceof String viewName) {
            ViewResolver viewResolver = findViewResolver(viewName);
            return viewResolver.resolve(viewName);
        }
        throw new IllegalStateException("Cannot Resolve View " + viewObject);
    }

    private ViewResolver findViewResolver(String viewName) {
        return viewResolvers.stream()
                .filter(viewResolver -> viewResolver.canResolve(viewName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No ViewResolver Found For " + viewName));
    }
}
