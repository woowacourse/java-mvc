package nextstep.mvc;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.controller.tobe.HandlerExecution;
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

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());
        //todo: adaptor pattern 적용
        try {
            final Object controller = getController(request);
            renderString(request, response, controller);
            renderModelAndView(request, response, controller);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void renderModelAndView(HttpServletRequest request, HttpServletResponse response, Object controller)
            throws Exception {
        if (controller instanceof HandlerExecution) {
            HandlerExecution execution = (HandlerExecution) controller;
            ModelAndView modelAndView = execution.handle(request, response);
            View view = modelAndView.getView();
            view.render(modelAndView.getModel(), request, response);

        }
    }

    private void renderString(HttpServletRequest request, HttpServletResponse response, Object controller)
            throws Exception {
        if (controller instanceof Controller) {
            Controller controller2 = (Controller) controller;
            final String viewName = controller2.execute(request, response);
            move(viewName, request, response);
        }
    }

    private Object getController(HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow();
    }

    private void move(String viewName, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
            return;
        }

        final RequestDispatcher requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }
}
