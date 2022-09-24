package nextstep.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import nextstep.mvc.adpater.HandlerAdapter;
import nextstep.mvc.adpater.HandlerAdapterRegistry;
import nextstep.mvc.exception.NoHandlerFoundException;
import nextstep.mvc.view.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);
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

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        handlerAdapterRegistry.add(handlerAdapter);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            final Optional<Object> handler = handlerMappingRegistry.getHandler(request);
            if (handler.isEmpty()) {
                noHandlerFound(request, response);
                return;
            }

            final Object handlerInstance = handler.get();
            final HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handlerInstance);
            final ModelAndView modelAndView = handlerAdapter.handle(request, response, handlerInstance);
            modelAndView.render(request, response);
        } catch (final Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void noHandlerFound(final HttpServletRequest request, final HttpServletResponse response)
            throws IOException {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
        throw new NoHandlerFoundException(request.getMethod(), request.getRequestURI());
    }
}
