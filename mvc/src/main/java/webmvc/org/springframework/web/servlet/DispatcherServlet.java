package webmvc.org.springframework.web.servlet;

import webmvc.org.springframework.web.servlet.mvc.adapter.HandlerAdapterRegistry;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMappingRegistry;
import webmvc.org.springframework.web.servlet.mvc.tobe.Request;
import webmvc.org.springframework.web.servlet.view.View;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final transient HandlerMappingRegistry handlerMappingRegistry;
    private final transient HandlerAdapterRegistry handlerAdapterRegistry;

    public DispatcherServlet() {
        handlerMappingRegistry = new HandlerMappingRegistry();
        handlerAdapterRegistry = new HandlerAdapterRegistry();
    }

    @Override
    public void init() {
        handlerMappingRegistry.initialize(getClass().getPackageName());
        handlerAdapterRegistry.initialize();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        final Request customRequest = new Request(requestURI, request.getMethod());
        final Object handler = handlerMappingRegistry.getHandler(customRequest);
        final ModelAndView modelAndView = handlerAdapterRegistry.handle(handler, request, response);

        render(modelAndView, request, response);
    }

    private void render(final ModelAndView modelAndView, final HttpServletRequest request, final HttpServletResponse response) {
        final View view = modelAndView.getView();
        try {
            view.render(modelAndView.getModel(), request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
