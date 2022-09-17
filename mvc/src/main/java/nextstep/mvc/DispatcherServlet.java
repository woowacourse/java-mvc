package nextstep.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.RedirectView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final List<HandlerMapping> handlerMappings;

    public DispatcherServlet() {
        this.handlerMappings = new ArrayList<>();
    }

    @Override
    public void init() {
        log.info("Initialize DispatcherServlet!");
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
        log.info("Add HandlerMapping: {}", handlerMapping);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            final var handler = getHandler(request);
            ModelAndView modelAndView = getModelAndView(request, response, handler);
            move(modelAndView, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private ModelAndView getModelAndView(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        ModelAndView modelAndView = null;
        if (handler instanceof Controller) {
            String viewName = ((Controller) handler).execute(request, response);
            modelAndView = toModelAndView(viewName);
        }
        if (handler instanceof HandlerExecution) {
            modelAndView = (ModelAndView) ((HandlerExecution) handler).handle(request, response);
        }
        return modelAndView;
    }

    private ModelAndView toModelAndView(String viewName) {
        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            return new ModelAndView(new RedirectView(viewName));
        }
        return new ModelAndView(new JspView(viewName));
    }

    private Object getHandler(final HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow();
    }

    private void move(final ModelAndView modelAndView, final HttpServletRequest request,
                      final HttpServletResponse response)
            throws Exception {
        if (modelAndView != null) {
            final var view = modelAndView.getView();
            view.render(modelAndView.getModel(), request, response);
        }
    }
}
