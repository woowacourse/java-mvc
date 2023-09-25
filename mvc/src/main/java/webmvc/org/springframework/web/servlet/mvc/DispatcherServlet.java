package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.adapter.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.adapter.HandlerAdapterRegistry;
import webmvc.org.springframework.web.servlet.mvc.mapper.HandlerMappingRegistry;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final transient HandlerMappingRegistry handlerMappingRegistry;
    private final transient HandlerAdapterRegistry handlerAdapterRegistry;

    public DispatcherServlet(
            final HandlerMappingRegistry handlerMappingRegistry,
            final HandlerAdapterRegistry handlerAdapterRegistry
    ) {
        this.handlerMappingRegistry = handlerMappingRegistry;
        this.handlerAdapterRegistry = handlerAdapterRegistry;
    }

    @Override
    public void init() {
        handlerMappingRegistry.init();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        try {
            final Object handler = handlerMappingRegistry.getHandler(request);
            final HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);
            final ModelAndView mav = handlerAdapter.handle(handler, request, response);
            mav.getView().render(mav.getModel(), request, response);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
