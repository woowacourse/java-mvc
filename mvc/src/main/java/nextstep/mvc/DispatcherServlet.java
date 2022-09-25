package nextstep.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import nextstep.mvc.controller.HandlerMappingRegistry;
import nextstep.mvc.view.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegistry handlerMappingRegistry;
    private final HandlerExecutor handlerExecutor;

    public DispatcherServlet() {
        this.handlerMappingRegistry = new HandlerMappingRegistry();
        this.handlerExecutor = new HandlerExecutor(new HandlerAdapterRegistry());
    }

    @Override
    public void init() {
        handlerMappingRegistry.initialize();
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMappingRegistry.add(handlerMapping);
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        handlerExecutor.addHandlerAdapter(handlerAdapter);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            final Optional<Object> handler = handlerMappingRegistry.getHandler(request);
            if (handler.isEmpty()) {
                response.setStatus(404);
                response.sendRedirect("/404.jsp");
                return;
            }

            final ModelAndView modelAndView = handlerExecutor.handle(request, response, handler.get());
            modelAndView.render(request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
