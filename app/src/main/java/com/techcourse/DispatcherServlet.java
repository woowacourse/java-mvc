package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.tobe.adapter.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.adapter.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.mvc.tobe.exception.HandlerAdapterNotFoundException;
import com.interface21.webmvc.servlet.mvc.tobe.exception.HandlerMappingNotFoundException;
import com.interface21.webmvc.servlet.mvc.tobe.mapping.HandlerMappingRegistry;
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

    private final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry(
            DispatcherServlet.class.getPackage());
    private final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();

    @Override
    public void init() {
        handlerMappingRegistry.addHandlerMapping(new ManualHandlerMapping());
        handlerAdapterRegistry.addHandlerAdapter(new ManualHandlerAdapter());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            handleRequest(request, response);
        } catch (Exception e) {
            log.error("Servlet Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage(), e);
        }
    }

    private void handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            Object handler = handlerMappingRegistry.getHandler(request);
            HandlerAdapter adapter = handlerAdapterRegistry.getHandlerAdapter(handler);
            ModelAndView modelAndView = adapter.handle(request, response, handler);
            renderView(request, response, modelAndView);

        } catch (HandlerMappingNotFoundException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } catch (HandlerAdapterNotFoundException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void renderView(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView)
            throws Exception {
        if (modelAndView == null || modelAndView.getView() == null) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        View view = modelAndView.getView();
        view.render(modelAndView.getModel(), request, response);
    }
}
