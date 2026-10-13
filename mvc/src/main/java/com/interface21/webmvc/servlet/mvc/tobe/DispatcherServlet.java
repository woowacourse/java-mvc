package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final Object[] basePackages;
    private HandlerMappingRegistry handlerMappingRegistry;
    private HandlerAdapterRegistry handlerAdapterRegistry;


    public DispatcherServlet(Object... basePackages) {
        this.basePackages = basePackages;
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMappingRegistry.addHandlerMapping(handlerMapping);
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        handlerAdapterRegistry.addHandlerAdapter(handlerAdapter);
    }

    @Override
    public void init() {
        final AnnotationHandlerMapping annotationHandlerMapping =
            new AnnotationHandlerMapping(basePackages);
        annotationHandlerMapping.initialize();

        handlerMappingRegistry = HandlerMappingRegistry.empty();
        addHandlerMapping(annotationHandlerMapping);

        handlerAdapterRegistry = HandlerAdapterRegistry.empty();
        addHandlerAdapter(new HandlerExecutionAdapter());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            final Object handler = handlerMappingRegistry.getHandler(request)
                .orElseThrow(() -> new HandlerNotFoundException(String.format(
                    "핸들러를 찾지 못했습니다 (Method : %s, Request URI : %s)",
                    request.getMethod(),
                    request.getRequestURI())));
            final HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);
            final ModelAndView mav = handlerAdapter.adapt(request, response, handler);

            render(mav, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void render(final ModelAndView mav, final HttpServletRequest request,
        final HttpServletResponse response)
        throws Exception {
        final View view = mav.getView();
        view.render(mav.getModel(), request, response);
    }
}
