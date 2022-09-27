package nextstep.mvc;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.handlerAdapter.HandlerAdapter;
import nextstep.mvc.handlerAdapter.HandlerAdapterRegistry;
import nextstep.mvc.handlerMapping.HandlerMapping;
import nextstep.mvc.handlerMapping.HandlerMappingRegistry;
import nextstep.mvc.view.ModelAndView;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegistry handlerMappingRegistry;
    private final HandlerAdapterRegistry handlerAdapterRegistry;

    public DispatcherServlet() {
        handlerMappingRegistry = new HandlerMappingRegistry();
        handlerAdapterRegistry = new HandlerAdapterRegistry();
    }

    @Override
    public void init() {
        handlerMappingRegistry.init();
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        handlerAdapterRegistry.addHandlerAdapter(handlerAdapter);
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMappingRegistry.addHandlerMapping(handlerMapping);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws
        ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {

            Optional<Object> handler = handlerMappingRegistry.getHandler(request);

            if (handler.isEmpty()) {
                response.sendRedirect("/404.jsp");
                return;
            }

            HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler.get());
            ModelAndView modelAndView = handlerAdapter.handle(request, response, handler.get());
            modelAndView.render(request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

}
