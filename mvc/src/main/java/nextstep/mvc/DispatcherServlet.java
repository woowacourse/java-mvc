package nextstep.mvc;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.handler.adaptor.HandlerAdapter;
import nextstep.mvc.handler.adaptor.HandlerAdapters;
import nextstep.mvc.handler.mapping.HandlerMapping;
import nextstep.mvc.handler.mapping.HandlerMappings;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.ViewResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappings handlerMappings;
    private final HandlerAdapters handlerAdapters;

    public DispatcherServlet() {
        this.handlerMappings = new HandlerMappings();
        this.handlerAdapters = new HandlerAdapters();
    }

    @Override
    public void init() {
        handlerMappings.initialize();
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        this.handlerMappings.add(handlerMapping);
    }

    public void addHandlerAdaptor(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            Optional<Object> handlerMapping = handlerMappings.get(request);
            if (handlerMapping.isEmpty()) {
                render(request, response, "404");
                return;
            }

            Object handler = handlerMapping.get();
            HandlerAdapter handlerAdapter = handlerAdapters.get(handler)
                .orElseThrow();

            ModelAndView mv = handlerAdapter.handle(request, response, handler);

            ViewResolver.resolve(mv, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            render(request, response, "500");
        }
    }

    private void render(HttpServletRequest request, HttpServletResponse response, String viewName)
        throws ServletException, IOException {
        final RequestDispatcher requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }
}
