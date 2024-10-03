package com.interface21.webmvc.servlet.mvc;

import java.util.Objects;
import java.util.Optional;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.tobe.adapter.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.mapping.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.handler.ErrorController;
import com.interface21.webmvc.servlet.mvc.tobe.adapter.ErrorHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.adapter.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.adapter.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.mvc.tobe.mapping.HandlerMappingRegistry;
import com.interface21.webmvc.servlet.view.JsonViewResolver;
import com.interface21.webmvc.servlet.view.JspViewResolver;
import com.interface21.webmvc.servlet.view.ViewResolverRegistry;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerAdapterRegistry handlerAdapterRegistry;
    private final HandlerMappingRegistry handlerMappingRegistry;
    private final ViewResolverRegistry viewResolverRegistry;

    public DispatcherServlet() {
        this.handlerAdapterRegistry = new HandlerAdapterRegistry();
        this.handlerMappingRegistry = new HandlerMappingRegistry();
        this.viewResolverRegistry = new ViewResolverRegistry();
    }

    @Override
    public void init() {
        initHandlerAdapter();
        initHandlerMapping();
        initViewResolver();
    }

    private void initViewResolver() {
        JsonViewResolver jsonViewResolver = new JsonViewResolver();
        JspViewResolver jspViewResolver = new JspViewResolver();
        viewResolverRegistry.addViewResolver(jsonViewResolver);
        viewResolverRegistry.addViewResolver(jspViewResolver);
    }

    private void initHandlerMapping() {
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("com");
        annotationHandlerMapping.initialize();
        handlerMappingRegistry.addHandlerMapping(annotationHandlerMapping);
    }

    private void initHandlerAdapter() {
        AnnotationHandlerAdapter annotationHandlerAdapter = new AnnotationHandlerAdapter();
        ErrorHandlerAdapter errorHandlerAdapter = new ErrorHandlerAdapter();
        handlerAdapterRegistry.addHandlerAdapter(annotationHandlerAdapter);
        handlerAdapterRegistry.addHandlerAdapter(errorHandlerAdapter);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            Optional<Object> unExpectedHandler = handlerMappingRegistry.getHandler(request);
            Object handler = unExpectedHandler.orElseGet(ErrorController::new);
            final HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);
            final ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);
            render(modelAndView, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void render(final ModelAndView modelAndView, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        View view = modelAndView.getView();
        if (Objects.nonNull(view)) {
            view.render(modelAndView.getModel(), request, response);
            return;
        }
        String viewName = modelAndView.getViewName();
        View resolvedView = viewResolverRegistry.resolveViewName(viewName);
        resolvedView.render(modelAndView.getModel(), request, response);
    }
}
