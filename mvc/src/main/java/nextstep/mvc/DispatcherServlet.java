package nextstep.mvc;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.exception.NotFoundException;
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

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            Object handler = getHandlerMapping(request)
                .orElseThrow(NotFoundException::new);

            HandlerAdapter handlerAdapter = getHandlerAdapter(handler)
                .orElseThrow();

            ModelAndView mv = handlerAdapter.handle(request, response, handler);

            resolveView(mv, request, response);
        } catch (NotFoundException e) {
            log.error("Exception : {}", e.getMessage());
            render("404", request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            render("500", request, response);
        }
    }

    private Optional<Object> getHandlerMapping(HttpServletRequest request) {
        return this.handlerMappings.stream()
            .map(handlerMapping -> handlerMapping.getHandler(request))
            .filter(Objects::nonNull)
            .findAny();
    }

    private Optional<HandlerAdapter> getHandlerAdapter(Object handler) {
        return this.handlerAdapters.stream()
            .filter(handlerAdapter -> handlerAdapter.supports(handler))
            .findAny();
    }

    private void resolveView(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        View view = modelAndView.getView();
        view.render(modelAndView.getModel(), request, response);
    }

    private void render(String viewName, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
            return;
        }

        final RequestDispatcher requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }
}
