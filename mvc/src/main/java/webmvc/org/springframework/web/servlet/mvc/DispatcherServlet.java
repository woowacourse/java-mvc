package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.mvc.handleradapter.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.handleradapter.HandlerAdapters;
import webmvc.org.springframework.web.servlet.mvc.handlermapping.HandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.handlermapping.HandlerMappings;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappings handlerMappings;
    private final HandlerAdapters handlerAdapters;

    public DispatcherServlet() {
        handlerMappings = new HandlerMappings();
        handlerAdapters = new HandlerAdapters();
    }

    @Override
    public void init() {
        handlerMappings.initialize();
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final Object handler = handlerMappings.getHandler(request);
            final HandlerAdapter handlerAdapter = handlerAdapters.getHandlerAdapter(handler);
            final var modelAndView = handlerAdapter.handle(request, response, handler);

            modelAndView.render(request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
