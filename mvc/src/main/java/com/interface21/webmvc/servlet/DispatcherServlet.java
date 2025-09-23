package com.interface21.webmvc.servlet;

import com.interface21.webmvc.servlet.mvc.handler.adapter.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.handler.adapter.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.handler.mapping.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.view.JspView;
import com.interface21.webmvc.servlet.view.ModelAndView;
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
    private static final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
    private static final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();
    private final String basePackage;

    public DispatcherServlet(String basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    public void init() {
        handlerMappingRegistry.addHandlerMapping(new AnnotationHandlerMapping(basePackage));
        handlerAdapterRegistry.addHandlerAdapter(new AnnotationHandlerAdapter());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            ModelAndView modelAndView = processRequest(request, response);
            modelAndView.render(request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private ModelAndView processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Optional<Object> handlerOptional = handlerMappingRegistry.getHandler(request);
        if (handlerOptional.isPresent()) {
            Object handler = handlerOptional.get();
            HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);
            return handlerAdapter.handler(handler, request, response);
        }
        return new ModelAndView(new JspView("redirect:/404.jsp"));
    }
}
