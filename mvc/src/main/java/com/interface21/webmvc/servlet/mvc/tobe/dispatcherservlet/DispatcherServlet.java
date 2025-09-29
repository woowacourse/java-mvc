package com.interface21.webmvc.servlet.mvc.tobe.dispatcherservlet;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.handleradapter.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.handleradapter.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.mvc.tobe.handlermapping.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.handlermapping.HandlerMappingRegistry;
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

    public DispatcherServlet(final HandlerMappingRegistry handlerMappingRegistry,
                             final HandlerAdapterRegistry handlerAdapterRegistry) {
        this.handlerMappingRegistry = handlerMappingRegistry;
        this.handlerAdapterRegistry = handlerAdapterRegistry;
        log.info("DispatcherServlet 생성");
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) {
        try {
            final HandlerMapping handlerMapping = handlerMappingRegistry.getHandlerMapping(request);
            final Object handler = handlerMapping.getHandler(request);
            log.info(String.format("%s %s 요청에 대한 handler 찾기 성공: %s", request.getRequestURI(), request.getMethod(),
                    handler.getClass().getSimpleName()));

            final HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);
            log.info(String.format("%s %s 요청에 대한 adapter 찾기 성공: %s", request.getRequestURI(), request.getMethod(),
                    handlerAdapter.getClass().getSimpleName()));
            final ModelAndView mav = handlerAdapter.handle(handler, request, response);

            mav.render(request, response);
        } catch (Exception e) {
            throw new IllegalStateException(
                    String.format("%s %s 요청에 대한 핸들러를 처리하는데 실패했습니다.", request.getRequestURI(), request.getMethod()));
        }
    }
}
