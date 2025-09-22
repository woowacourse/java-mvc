package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.NoHandlerFoundException;
import com.interface21.webmvc.servlet.mvc.tobe.handler.adapter.HandlerAdapterRegistry;
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

    private final HandlerMappingRegistry handlermappingRegistry;
    private final HandlerAdapterRegistry handlerAdapterRegistry;

    public DispatcherServlet() {
        this.handlermappingRegistry = new HandlerMappingRegistry();
        this.handlerAdapterRegistry = new HandlerAdapterRegistry();
    }

    @Override
    protected void service(
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            final var handler = handlermappingRegistry.getHandler(request);
            final var handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);
            var modelAndView = handlerAdapter.handle(request, response, handler);
            render(modelAndView, request, response);
        } catch (final NoHandlerFoundException e) {
            log.warn("No Handler Found Exception : {}", e.getMessage());
            noHandlerFound(response);
        } catch (final Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void noHandlerFound(final HttpServletResponse response) throws ServletException {
        try {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } catch (final IOException e) {
            throw new ServletException(e.getMessage(), e);
        }
    }

    private void render(final ModelAndView mav, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        var view = mav.getView();
        var model = mav.getModel();
        view.render(model, request, response);
    }
}
