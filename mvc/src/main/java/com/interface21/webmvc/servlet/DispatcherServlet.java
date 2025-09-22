package com.interface21.webmvc.servlet;

import com.interface21.webmvc.servlet.handler.adapter.HandlerAdapter;
import com.interface21.webmvc.servlet.handler.adapter.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.handler.mapping.HandlerMapping;
import com.interface21.webmvc.servlet.handler.mapping.HandlerMappingRegistry;
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
    private final HandlerAdapterRegistry handlerAdapterRegistry;

    public DispatcherServlet() {
        this.handlerMappingRegistry = HandlerMappingRegistry.empty();
        this.handlerAdapterRegistry = HandlerAdapterRegistry.empty();
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        handlerAdapterRegistry.addHandlerAdapter(handlerAdapter);
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMappingRegistry.addHandlerMapping(handlerMapping);
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

        /*
        1. HandlerMapping 에서 request를 처리할 수 있는 핸들러 조회
        2. HandlerAdapterRegistry 에서 핸들러를 처리할 수 있는 핸들러 어댑터 조회
        3. 핸들러 어댑터를 통해 핸들러 실행 -> ModelAndView 반환
        4. ModelAndView를 통해 View 렌더링
        * */
        try {
            final var handler = getHandler(request);
            final var modelAndView = executeHandler(handler, request, response);
            View view = modelAndView.getView();

            view.render(modelAndView.getModel(), request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object getHandler(final HttpServletRequest request) {
        return handlerMappingRegistry.getHandler(request)
                .orElseThrow(() -> new RuntimeException("cannot find handler for " + request.getRequestURI()));
    }

    private ModelAndView executeHandler(final Object handler, final HttpServletRequest request,
                                        final HttpServletResponse response)
            throws Exception {
        HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);

        return handlerAdapter.handle(request, response, handler);
    }
}
