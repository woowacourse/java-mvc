package nextstep.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import nextstep.mvc.adapter.HandlerAdapter;
import nextstep.mvc.view.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final List<HandlerMapping> handlerMappings;
    private final List<HandlerAdapter> handlerAdapters;

    public DispatcherServlet() {
        this.handlerMappings = new ArrayList<>();
        this.handlerAdapters = new ArrayList<>();
    }

    @Override
    public void init() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            doProcess(request, response);

        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void doProcess(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final Object handler = getHandler(request);
        final HandlerAdapter handlerAdapter = getHandlerAdapter(handler);
        final ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);

        modelAndView.render(request, response);
    }

    private Object getHandler(final HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findAny()
                .orElseThrow();
    }

    private HandlerAdapter getHandlerAdapter(final Object handler) {
        return handlerAdapters.stream()
                .filter(it -> it.supports(handler))
                .findAny()
                .orElseThrow();
    }
}
