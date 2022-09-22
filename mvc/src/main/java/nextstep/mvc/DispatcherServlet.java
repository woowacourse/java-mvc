package nextstep.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            process(request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Object handler = getHandler(request);
        if (handler instanceof Controller) {
            processManual(request, response, (Controller) handler);
            return;
        }
        processAnnotation(request, response, (HandlerExecution) handler);
    }

    private void processAnnotation(HttpServletRequest request, HttpServletResponse response, HandlerExecution handler) throws ServletException, IOException {
        ModelAndView modelAndView = handler.handle(request, response);
        View view = modelAndView.getView();
        Map<String, Object> model = modelAndView.getModel();
        view.render(model, request, response);
    }

    private void processManual(HttpServletRequest request, HttpServletResponse response, Controller handler) throws ServletException, IOException {
        String viewName = handler.execute(request, response);
        move(new JspView(viewName), request, response);
    }

    private Object getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("대응되는 handler가 없습니다"));
    }

    private void move(View view, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (view.isRedirect()) {
            response.sendRedirect(view.getTrimName());
            return;
        }
        final var requestDispatcher = request.getRequestDispatcher(view.getTrimName());
        requestDispatcher.forward(request, response);
    }
}
