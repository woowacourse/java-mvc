package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.mvc.HandlerMappingRegistry;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private static final long serialVersionUID = 1L;

    private HandlerMappingRegistry handlerMappingRegistry;
    private HandlerAdapterRegistry handlerAdapterRegistry;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        handlerMappingRegistry = WebMvcConfiguration.handlerMappingRegistry();
        handlerAdapterRegistry = WebMvcConfiguration.handlerAdapterRegistry();
    }

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse res) {
        final var optionalHandler = handlerMappingRegistry.getHandler(req);
        if (optionalHandler.isEmpty()) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            log.debug("Handler is not Found : {}", req.getRequestURI());
            return;
        }

        final var handler = optionalHandler.get();
        final var adapter = handlerAdapterRegistry.getAdapter(handler);

        final var modelAndView = adapter.handle(handler, req, res);

        render(modelAndView, req, res);
    }

    // TODO: View 랜더링 로직을 ViewResolver로 분리 필요 = Step 3 [2025-09-18]
    private void render(final ModelAndView modelAndView,
                        final HttpServletRequest req,
                        final HttpServletResponse res) {
        try {
            log.debug("Rendering View: {}", req.getRequestURI());

            final var view = modelAndView.getView();
            view.render(modelAndView.getModel(), req, res);
        } catch (Exception e) {
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new IllegalArgumentException("Rendering fail: " + req.getRequestURI(), e);
        }
    }
}
