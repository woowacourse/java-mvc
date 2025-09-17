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
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) {
        try {
            final HandlerMapping handlerMapping = handlerMappingRegistry.getHandlerMapping(request);
            final Object handler = handlerMapping.getHandler(request);

            final HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);
            final ModelAndView mav = handlerAdapter.handle(handler, request, response);

            render(mav, request, response);
        } catch (Exception e) {
            throw new IllegalStateException(
                    String.format("%s %s 요청에 대한 핸들러를 처리하는데 실패했습니다.", request.getRequestURI(), request.getMethod()));
        }
    }

    private void render(final ModelAndView modelAndView, final HttpServletRequest request,
                        final HttpServletResponse response) {
        try {
            modelAndView.getView().render(modelAndView.getModel(), request, response);
        } catch (Exception e) {
            throw new IllegalStateException(
                    String.format("%s %s 요청에 대한 응답을 렌더링하는데 실패했습니다.", request.getRequestURI(),
                            request.getMethod()));
        }
    }
}
