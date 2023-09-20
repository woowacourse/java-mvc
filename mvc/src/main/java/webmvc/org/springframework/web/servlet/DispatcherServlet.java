package webmvc.org.springframework.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.mvc.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.HandlerAdapterRegistry;
import webmvc.org.springframework.web.servlet.mvc.HandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.HandlerMappingRegistry;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final transient HandlerMappingRegistry handlerMappingRegistry;
    private final transient HandlerAdapterRegistry handlerAdapterRegistry;

    public static FirstBuilder builder() {
        return new FirstBuilder();
    }

    public static class FirstBuilder {

        private FirstBuilder() {
        }

        public SecondBuilder addHandlerMapping(final HandlerMapping handlerMapping) {
            return new SecondBuilder().addHandlerMapping(handlerMapping);
        }
    }

    public static class SecondBuilder {

        private final HandlerMappingRegistry handlerMappingRegistry;
        private final HandlerAdapterRegistry handlerAdapterRegistry;

        private SecondBuilder() {
            this.handlerMappingRegistry = new HandlerMappingRegistry();
            this.handlerAdapterRegistry = new HandlerAdapterRegistry();
        }

        public SecondBuilder addHandlerMapping(final HandlerMapping handlerMapping) {
            handlerMappingRegistry.addHandlerMapping(handlerMapping);
            return this;
        }

        public LastBuilder addHandlerAdapter(final HandlerAdapter handlerAdapter) {
            handlerAdapterRegistry.addHandlerAdapter(handlerAdapter);
            return new LastBuilder(handlerMappingRegistry, handlerAdapterRegistry);
        }
    }

    public static class LastBuilder {

        private final HandlerMappingRegistry handlerMappings;
        private final HandlerAdapterRegistry handlerAdapterRegistry;

        private LastBuilder(final HandlerMappingRegistry handlerMappings,
                           final HandlerAdapterRegistry handlerAdapterRegistry) {
            this.handlerMappings = handlerMappings;
            this.handlerAdapterRegistry = handlerAdapterRegistry;
        }

        public LastBuilder addHandlerMapping(final HandlerMapping handlerMapping) {
            handlerMappings.addHandlerMapping(handlerMapping);
            return this;
        }

        public LastBuilder addHandlerAdapter(final HandlerAdapter handlerAdapter) {
            handlerAdapterRegistry.addHandlerAdapter(handlerAdapter);
            return this;
        }

        public DispatcherServlet build() {
            return new DispatcherServlet(this);
        }

        private HandlerMappingRegistry getHandlerMappings() {
            return handlerMappings;
        }

        private HandlerAdapterRegistry getHandlerAdapterRegistry() {
            return handlerAdapterRegistry;
        }
    }

    private DispatcherServlet(final LastBuilder builder) {
        this.handlerMappingRegistry = builder.getHandlerMappings();
        this.handlerAdapterRegistry = builder.getHandlerAdapterRegistry();
    }

    @Override
    public void init() {
        handlerMappingRegistry.initialize();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final Object handler = handlerMappingRegistry.findHandler(request);
            final HandlerAdapter handlerAdapter = handlerAdapterRegistry.findHandlerAdapter(handler);
            final ModelAndView modelAndView = handlerAdapter.handle(handler, request, response);
            resolveView(modelAndView, request, response);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void resolveView(final ModelAndView modelAndView, final HttpServletRequest request,
                             final HttpServletResponse response) throws Exception {
        final View view = modelAndView.getView();
        final Map<String, Object> model = modelAndView.getModel();
        view.render(model, request, response);
    }
}
