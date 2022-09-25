package nextstep.mvc;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerAdapterRegistry handlerAdapterRegistry;
    private final HandlerMappingRegistry handlerMappingRegistry;

    public DispatcherServlet() {
        this.handlerAdapterRegistry = new HandlerAdapterRegistry();
        this.handlerMappingRegistry = new HandlerMappingRegistry();
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        this.handlerAdapterRegistry.addHandlerAdapter(handlerAdapter);
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        this.handlerMappingRegistry.addHandlerMapping(handlerMapping);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        handlerMappingRegistry.initialize();
        super.init(config);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        final var handler = getHandler(request);
        final var handlerAdapter = getHandlerAdapter(handler);
        ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);
        modelAndView.render(request, response);
    }

    private HandlerAdapter getHandlerAdapter(final Object handler) {
        return handlerAdapterRegistry.getHandlerAdapter(handler);
    }

    private Object getHandler(final HttpServletRequest request) {
        return handlerMappingRegistry.getHandler(request);
    }
}
