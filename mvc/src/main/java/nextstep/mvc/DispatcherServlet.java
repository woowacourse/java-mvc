package nextstep.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            final var controller = getController(request);
            final HandlerAdapter adapter = getAdapter(controller);
            final ModelAndView mv = adapter.handle(request, response, controller);
            mv.render(request, response);

        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private Controller getController(final HttpServletRequest request) throws ServletException {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .map(Controller.class::cast)
                .findFirst()
                .orElseThrow(() -> new ServletException("처리할 수 있는 handler를 찾지 못했습니다."));
    }

    private HandlerAdapter getAdapter(final Controller controller) throws ServletException {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(controller))
                .findAny()
                .orElseThrow(() -> new ServletException("처리할 수 있는 handlerAdapter를 찾지 못했습니다."));
    }
}
