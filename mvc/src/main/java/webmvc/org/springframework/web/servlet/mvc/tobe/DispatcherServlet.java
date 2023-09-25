package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);
    private final String basePackage;

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();

    public DispatcherServlet(final String basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    public void init() {
        handlerMappings.add(new AnnotationHandlerMapping(basePackage));
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            final var controller = getHandler(request);
            final var modelAndView = getModelAndView(controller, request, response);
            modelAndView.getView().render(modelAndView.getModel(), request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object getHandler(final HttpServletRequest request) throws ServletException {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findAny()
                .orElseThrow(() -> new ServletException("Failed to find appropriate handler for this request."));
    }

    private ModelAndView getModelAndView(final Object controller, final HttpServletRequest request,
                                         final HttpServletResponse response)
            throws Exception {
        if (controller instanceof HandlerExecution) {
            HandlerExecution handler = (HandlerExecution) controller;
            return handler.handle(request, response);
        }

        throw new IllegalStateException("can't response to this response");
    }
}
