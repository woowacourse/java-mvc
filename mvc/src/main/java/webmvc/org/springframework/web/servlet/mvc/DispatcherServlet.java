package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerAdapterRegistry handlerAdapterRegistry;
    private final HandlerMappingRegistry handlerMappingRegistry;

    public DispatcherServlet(final HandlerAdapterRegistry handlerAdapterRegistry, final HandlerMappingRegistry handlerMappingRegistry) {
        this.handlerAdapterRegistry = handlerAdapterRegistry;
        this.handlerMappingRegistry = handlerMappingRegistry;
    }

    @Override
    public void init() {
        handlerMappingRegistry.initialize();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final var handle = handlerMappingRegistry.getHandler(request);
            final var handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handle);
            final var modelAndView = handlerAdapter.handle(handle, request, response);
            modelAndView.render(request, response);
        } catch (IllegalArgumentException e) {
            log.error("Exception : {}", e.getMessage(), e);

            final var modelAndView = new ModelAndView(new JspView("/404.jsp"));
            response.setStatus(404);
            modelAndView.render(request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);

            final var modelAndView = new ModelAndView(new JspView("/500.jsp"));
            response.setStatus(500);
            modelAndView.render(request, response);
        }
    }
}
