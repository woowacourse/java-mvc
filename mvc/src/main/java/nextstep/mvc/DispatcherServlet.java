package nextstep.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.controller.tobe.exception.NotSupportHandler;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final List<HandlerMapping> handlerMappings;

    public DispatcherServlet() {
        this.handlerMappings = new ArrayList<>();
    }

    @Override
    public void init() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());
        final var handler = getHandler(request);

        if (handler instanceof Controller) {
            handleManualHandler(request, response, (Controller) handler);
        }
        if (handler instanceof HandlerExecution) {
            handleAnnotationHandler(request, response, (HandlerExecution) handler);
        }
    }

    private Object getHandler(final HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findAny()
                .orElseThrow(NotSupportHandler::new);
    }

    private void handleManualHandler(final HttpServletRequest request, final HttpServletResponse response,
                                     final Controller handler) throws ServletException {
        try {
            String viewName = handler.execute(request, response);
            final ModelAndView modelAndView = new ModelAndView(new JspView(viewName));
            renderView(modelAndView, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private static void handleAnnotationHandler(final HttpServletRequest request, final HttpServletResponse response,
                                                final HandlerExecution handler) {
        try {
            ModelAndView modelAndView = handler.handle(request, response);
            renderView(modelAndView, request, response);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    private static void renderView(final ModelAndView modelAndView, final HttpServletRequest request,
                                   final HttpServletResponse response) throws Exception {
        final Map<String, Object> model = modelAndView.getModel();
        final View view = modelAndView.getView();
        view.render(model, request, response);
    }
}
