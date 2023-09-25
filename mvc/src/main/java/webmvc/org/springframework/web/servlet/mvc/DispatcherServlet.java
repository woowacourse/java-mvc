package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
    private final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();
    private HandlerExecutor handlerExecutor;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        handlerExecutor = new HandlerExecutor(handlerAdapterRegistry);
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMappingRegistry.addHandlerMapping(handlerMapping);
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        handlerAdapterRegistry.addHandlerAdapter(handlerAdapter);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        final Optional<Object> nullableHandler = handlerMappingRegistry.getHandler(request);

        if (nullableHandler.isEmpty()) {
            final ModelAndView notFoundModelAndView = handlerExecutor.handleNotFound();
            render(notFoundModelAndView, request, response);
            return;
        }

        final Object handler = nullableHandler.get();
        try {
            final ModelAndView modelAndView = handlerExecutor.handle(request, response, handler);
            render(modelAndView, request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void render(final ModelAndView modelAndView,
                        HttpServletRequest request,
                        HttpServletResponse response
    ) throws ServletException {
        try {
            modelAndView.getView().render(modelAndView.getModel(), request, response);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
