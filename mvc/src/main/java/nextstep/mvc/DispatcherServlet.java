package nextstep.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import nextstep.mvc.controller.tobe.HandlerMappingRegistry;
import nextstep.mvc.exception.HandlerAdapterException;
import nextstep.mvc.exception.HandlerMappingException;
import nextstep.mvc.exception.ViewException;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegistry handlerMappingRegistry;
    private final List<HandlerAdapter> handlerAdapters;

    public DispatcherServlet() {
        this(new HandlerMappingRegistry(), new ArrayList<>());
    }

    private DispatcherServlet(final HandlerMappingRegistry handlerMappingRegistry,
                              final List<HandlerAdapter> handlerAdapters) {
        this.handlerMappingRegistry = handlerMappingRegistry;
        this.handlerAdapters = handlerAdapters;
    }

    @Override
    public void init() {
        handlerMappingRegistry.initialize();
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMappingRegistry.addHandlerMapping(handlerMapping);
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            final Object mappedHandler = getHandler(request);
            final HandlerAdapter mappedHandlerAdapter = getMappedHandlerAdapter(mappedHandler);
            final ModelAndView modelAndView = handle(request, response, mappedHandler, mappedHandlerAdapter);
            render(modelAndView, request, response);
        } catch (final Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object getHandler(final HttpServletRequest request) {
        final Optional<Object> handler = handlerMappingRegistry.getHandler(request);
        if (handler.isPresent()) {
            return handler.get();
        }
        throw new HandlerMappingException("A matching handler is not found.");
    }

    private HandlerAdapter getMappedHandlerAdapter(final Object mappedHandler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(mappedHandler))
                .findFirst()
                .orElseThrow(() -> new HandlerAdapterException("A matching handler adapter is not found."));
    }

    private ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response,
                                final Object mappedHandler, final HandlerAdapter mappedHandlerAdapter) {
        try {
            return mappedHandlerAdapter.handle(request, response, mappedHandler);
        } catch (final Exception e) {
            throw new HandlerAdapterException("Failed to handle a request.", e);
        }
    }

    private void render(final ModelAndView modelAndView, final HttpServletRequest request,
                        final HttpServletResponse response) {
        final View view = modelAndView.getView();
        final Map<String, Object> model = modelAndView.getModel();
        try {
            view.render(model, request, response);
        } catch (final Exception e) {
            throw new ViewException("Filed to render a view.", e);
        }
    }
}
