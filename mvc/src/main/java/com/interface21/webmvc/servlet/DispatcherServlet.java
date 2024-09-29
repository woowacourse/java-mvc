package com.interface21.webmvc.servlet;

import com.interface21.webmvc.servlet.handler.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.handler.HandlerExecutionHandlerAdapter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String BASE_PACKAGE = "com.techcourse.controller";

    private final HandlerMappingRegistry handlerMappingRegistry;
    private final HandlerAdapterRegistry handlerAdapterRegistry;

    public DispatcherServlet() {
        handlerMappingRegistry = new HandlerMappingRegistry();
        handlerAdapterRegistry = new HandlerAdapterRegistry();
    }

    @Override
    public void init() {
        handlerMappingRegistry.addHandlerMapping(new AnnotationHandlerMapping(BASE_PACKAGE));
        handlerAdapterRegistry.addHandlerAdapter(new HandlerExecutionHandlerAdapter());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            Object handler = handlerMappingRegistry.getHandler(request)
                    .orElseThrow(() -> new ServletException("요청에 해당하는 핸들러를 찾을 수 없습니다."));

            ModelAndView modelAndView = handlerAdapterRegistry.execute(request, response, handler)
                    .orElseThrow(() -> new ServletException("ModelAndView를 리턴할 수 없습니다."));

            View view = modelAndView.getView();
            view.render(modelAndView.getModel(), request, response);
        } catch (Throwable e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
