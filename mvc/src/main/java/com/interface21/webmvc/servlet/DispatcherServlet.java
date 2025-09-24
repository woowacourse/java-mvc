package com.interface21.webmvc.servlet;

import com.interface21.webmvc.servlet.mvc.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.mvc.HandlerMappingRegistry;
import com.interface21.webmvc.servlet.mvc.ManualHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
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

    private HandlerMappingRegistry mappingRegistry;
    private HandlerAdapterRegistry adapterRegistry;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        mappingRegistry = new HandlerMappingRegistry();
        adapterRegistry = new HandlerAdapterRegistry();

        // 매핑 등록
        mappingRegistry.addHandlerMapping(new AnnotationHandlerMapping());

        // 어댑터 등록
        adapterRegistry.addHandlerAdapter(new ManualHandlerAdapter());
        adapterRegistry.addHandlerAdapter(new AnnotationHandlerAdapter());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final Optional<Object> handlerOpt = mappingRegistry.getHandler(request);
            if (handlerOpt.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            final Object handler = handlerOpt.get();
            final HandlerAdapter handlerAdapter = adapterRegistry.getHandlerAdapter(handler);
            final ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);
            final View jspView = modelAndView.getView();

            jspView.render(modelAndView.getModel(), request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
