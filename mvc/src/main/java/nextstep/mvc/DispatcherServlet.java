package nextstep.mvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final List<HandlerMapping> handlerMappings;
    private final List<HandlerAdaptor> handlerAdaptors;

    public DispatcherServlet() {
        this.handlerMappings = new ArrayList<>();
        this.handlerAdaptors = new ArrayList<>();
    }

    @Override
    public void init() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public void addHandlerAdaptor(final HandlerAdaptor handlerAdaptor) {
        handlerAdaptors.add(handlerAdaptor);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws
        ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());
        try {
            final Object handler = getHandler(request);
            final HandlerAdaptor adaptor = getHandlerAdaptor(handler);
            final ModelAndView modelAndView = adaptor.handle(request, response, handler);

            modelAndView.render(request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object getHandler(final HttpServletRequest request) {
        return handlerMappings.stream()
            .map(handlerMapping -> handlerMapping.getHandler(request))
            .filter(Objects::nonNull)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Couldn't find a handler"));
    }

    private HandlerAdaptor getHandlerAdaptor(final Object handler) {
        return handlerAdaptors.stream()
            .filter(handlerAdaptor -> handlerAdaptor.supports(handler))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Couldn't find adaptor for handler"));
    }
}
