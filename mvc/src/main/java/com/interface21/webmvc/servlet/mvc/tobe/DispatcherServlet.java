package com.interface21.webmvc.servlet.mvc.tobe;

import java.io.IOException;
import java.util.NoSuchElementException;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.webmvc.servlet.HandlerAdapter;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String BASE_PACKAGE = "com.techcourse";

    private final HandlerAdapterRegistry handlerAdapterRegistry;
    private final HandlerMappingRegistry handlerMappingRegistry;

    public DispatcherServlet() {
        this.handlerAdapterRegistry = new HandlerAdapterRegistry();
        this.handlerMappingRegistry = new HandlerMappingRegistry();
    }

    @Override
    public void init() {
        handlerMappingRegistry.addHandlerMapping(new AnnotationHandlerMapping(BASE_PACKAGE));

        handlerAdapterRegistry.addHandlerAdapter(new HandlerExecutionHandlerAdapter());
        handlerAdapterRegistry.addHandlerAdapter(new ControllerHandlerAdapter());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws IOException {
        log.info("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            final Object controller = handlerMappingRegistry.getHandler(request);
            log.info("controller = {}", controller);
            final HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(controller);
            final ModelAndView modelAndView = handlerAdapter.handle(request, response, controller);
            move(request, response, modelAndView);
        } catch (final NoSuchElementException e) {
            log.error("Exception : {}", e.getMessage());
            response.sendRedirect("/404.jsp");
        } catch (final Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            response.sendRedirect("/500.jsp");
        }
    }

    private void move(final HttpServletRequest request, final HttpServletResponse response,
                      final ModelAndView modelAndView) throws Exception {
        final View view = modelAndView.getView();
        view.render(modelAndView.getModel(), request, response);
    }
}
