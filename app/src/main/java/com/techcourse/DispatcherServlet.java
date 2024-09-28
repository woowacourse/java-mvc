package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.adapter.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.adapter.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.mvc.tobe.mapping.HandlerMappingRegistry;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegistry handlerMappingRegistry;
    private final HandlerAdapterRegistry handlerAdapterRegistry;


    public DispatcherServlet(
            HandlerMappingRegistry handlerMappingRegistry,
            HandlerAdapterRegistry handlerAdapterRegistry
    ) {
        this.handlerMappingRegistry = handlerMappingRegistry;
        this.handlerAdapterRegistry = handlerAdapterRegistry;
    }

    @Override
    public void init() {
        handlerMappingRegistry.initialize();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());
        try {
            Object handler = handlerMappingRegistry.getHandler(request)
                    .orElseThrow(() -> new NoSuchElementException("요청을 처리 할 수 있는 핸들러를 조회하지 못했습니다"));
            HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);
            ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);
            modelAndView.render(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
