package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutor;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMappingRegistry;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerNotFoundException;
import com.interface21.webmvc.servlet.mvc.tobe.MethodNotAllowedException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegistry handlerMappingRegistry;
    private final HandlerAdapterRegistry handlerAdapterRegistry;
    private final HandlerExecutor handlerExecutor;

    public DispatcherServlet() {
        this.handlerMappingRegistry = new HandlerMappingRegistry();
        this.handlerAdapterRegistry = new HandlerAdapterRegistry();
        this.handlerExecutor = new HandlerExecutor(handlerAdapterRegistry);
    }

    // 어떤 HandlerMapping, HandlerAdapter를 쓸지는 외부(설정)에서 정하고, 서블릿은 받은 것을 사용만 한다
    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMappingRegistry.addHandlerMapping(handlerMapping);
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        handlerAdapterRegistry.addHandlerAdapter(handlerAdapter);
    }

    @Override
    public void init() {
        handlerMappingRegistry.initialize();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final Object handler = handlerMappingRegistry.getHandler(request);
            final ModelAndView modelAndView = handlerExecutor.handle(request, response, handler);
            render(modelAndView, request, response);
        } catch (HandlerNotFoundException e) {
            log.warn(e.getMessage());
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } catch (MethodNotAllowedException e) {
            log.warn(e.getMessage());
            response.setHeader("Allow", e.getAllowHeaderValue());
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void render(final ModelAndView modelAndView, final HttpServletRequest request,
                        final HttpServletResponse response) throws Exception {
        modelAndView.getView().render(modelAndView.getModel(), request, response);
    }
}
