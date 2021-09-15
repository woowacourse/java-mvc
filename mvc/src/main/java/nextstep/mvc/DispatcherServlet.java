package nextstep.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import nextstep.mvc.exception.NoSuchHandlerAdapterException;
import nextstep.mvc.exception.NoSuchHandlerException;
import nextstep.mvc.handler.adapter.HandlerAdapter;
import nextstep.mvc.handler.mapping.HandlerMapping;
import nextstep.mvc.view.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final Long SERIAL_VERSION_UID = 1L;

    private final List<HandlerMapping> handlerMappings;
    private final List<HandlerAdapter> handlerAdapters;

    public DispatcherServlet() {
        this(new ArrayList<>(), new ArrayList<>());
    }

    private DispatcherServlet(
        List<HandlerMapping> handlerMappings,
        List<HandlerAdapter> handlerAdapters
    ) {
        this.handlerMappings = handlerMappings;
        this.handlerAdapters = handlerAdapters;
    }

    @Override
    public void init() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public void addHandlerMappings(HandlerMapping... handlerMappings) {
        this.handlerMappings.addAll(List.of(handlerMappings));
    }

    public void addHandlerAdapters(HandlerAdapter... handlerAdapters) {
        this.handlerAdapters.addAll(List.of(handlerAdapters));
    }

    @Override
    protected void service(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException {
        LOG.debug("Method: {}, Request URI: {}", request.getMethod(), request.getRequestURI());

        try {
            final Object handler = getHandler(request);

            HandlerAdapter handlerAdapter = getHandlerAdapter(handler);
            ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);

            modelAndView.render(request, response);
        } catch (Throwable e) {
            LOG.error("Exception: {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
            .map(handlerMapping -> handlerMapping.getHandler(request))
            .filter(Objects::nonNull)
            .findFirst()
            .orElseThrow(NoSuchHandlerException::new);
    }

    private HandlerAdapter getHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
            .filter(handlerAdapter -> handlerAdapter.supports(handler))
            .findFirst()
            .orElseThrow(NoSuchHandlerAdapterException::new);
    }
}
