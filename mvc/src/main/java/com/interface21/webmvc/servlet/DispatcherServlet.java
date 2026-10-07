package com.interface21.webmvc.servlet;

import com.interface21.webmvc.servlet.mvc.HandlerAdaptorRegistry;
import com.interface21.webmvc.servlet.mvc.HandlerMappingRegistry;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
        handlerMappingRegistry.initialize();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final var handler = handlerMappingRegistry.getHandler(request)
                    .orElseThrow(() -> new IllegalStateException("요청에 대응하는 핸들러가 없습니다."));
            final var handlerAdaptor = handlerAdaptorRegistry.getHandlerAdaptor(handler);
            ModelAndView mav = handlerAdaptor.handle(request, response, handler);
            mav.getView().render(mav.getModel(), request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
