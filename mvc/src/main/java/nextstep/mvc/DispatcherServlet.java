package nextstep.mvc;

import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;
import nextstep.mvc.controller.registry.HandlerAdapterRegistry;
import nextstep.mvc.controller.registry.HandlerMappingRegistry;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.ViewResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegistry handlerMappingRegistry;
    private final HandlerAdapterRegistry handlerAdapterRegistry;

    public DispatcherServlet() {
        this.handlerMappingRegistry = new HandlerMappingRegistry();
        this.handlerAdapterRegistry = new HandlerAdapterRegistry();
    }

    @Override
    public void init() {
        handlerMappingRegistry.initialize();
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMappingRegistry.addHandlerMapping(handlerMapping);
    }

    public void addAdapterMapping(final HandlerAdapter handlerAdapter) {
        handlerAdapterRegistry.addHandlerAdapter(handlerAdapter);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            executeHandler(request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void executeHandler(final HttpServletRequest request,
                                final HttpServletResponse response) throws Exception {
        Optional<Object> handler = handlerMappingRegistry.getHandler(request);
        if (handler.isEmpty()) {
            response.setStatus(SC_NOT_FOUND);
            return;
        }
        HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler.get());

        ModelAndView modelAndView = handlerAdapter.handle(request, response, handler.get());
        ViewResolver.resolve(request, response, modelAndView);
    }
}
