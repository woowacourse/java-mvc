package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.HandlerMappingRegistry;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.Serial;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    @Serial
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegistry handlerMappingRegistry;
    private final HandlerAdapterRegistry handlerAdapterRegistry;

    public DispatcherServlet() {
        handlerMappingRegistry = new HandlerMappingRegistry();
        handlerAdapterRegistry = new HandlerAdapterRegistry();
    }

    @Override
    public void init() {
        handlerMappingRegistry.initialize();
        handlerAdapterRegistry.initialize();
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String method = request.getMethod();
        String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", method, requestURI);

        try {
            Object handler = findHandlerOf(request);
            HandlerAdapter adapter = findHandlerAdapterOf(handler);
            ModelAndView modelAndView = adapter.handle(request, response, handler);
            modelAndView.render(request, response);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object findHandlerOf(HttpServletRequest request) {
        return handlerMappingRegistry.getHandler(request)
                .orElseThrow(() -> new IllegalArgumentException("요청에 해당하는 핸들러를 찾을 수 없습니다."));
    }

    private HandlerAdapter findHandlerAdapterOf(Object handler) {
        return handlerAdapterRegistry.getHandlerAdapter(handler)
                .orElseThrow(() -> new IllegalArgumentException("요청에 해당하는 핸들러 어댑터를 찾을 수 없습니다."));
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappingRegistry.addHandlerMapping(handlerMapping);
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapterRegistry.addHandlerAdapter(handlerAdapter);
    }
}
