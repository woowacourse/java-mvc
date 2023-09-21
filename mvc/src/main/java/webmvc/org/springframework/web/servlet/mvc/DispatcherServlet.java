package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.View;

import java.util.Optional;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegistry handlerMappingRegistry;
    private final HandlerAdapterRegistry handlerAdapterRegistry;
    private HandlerExecutor handlerExecutor;

    public DispatcherServlet() {
        this.handlerMappingRegistry = new HandlerMappingRegistry();
        this.handlerAdapterRegistry = new HandlerAdapterRegistry();
    }

    @Override
    public void init() {
        handlerMappingRegistry.initialize();
        this.handlerExecutor = new HandlerExecutor(handlerAdapterRegistry);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        Optional<Object> handler = handlerMappingRegistry.getHandler(request);

        if (handler.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        ModelAndView modelAndView = handlerExecutor.handle(request, response, handler.get());
        try {
            render(modelAndView, request, response);
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void render(final ModelAndView modelAndView, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        View view = modelAndView.getView();

        if (view == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        view.render(modelAndView.getModel(), request, response);
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapterRegistry.addHandlerAdapter(handlerAdapter);
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappingRegistry.addHandlerMapping(handlerMapping);
    }
}
