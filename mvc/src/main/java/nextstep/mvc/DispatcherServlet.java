package nextstep.mvc;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import nextstep.mvc.view.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerAdapterRegistry handlerAdapterRegistry;
    private final HandlerMappingRegistry handlerMappingRegistry;

    public DispatcherServlet() {
        this.handlerAdapterRegistry = new HandlerAdapterRegistry();
        this.handlerMappingRegistry = new HandlerMappingRegistry();
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        this.handlerAdapterRegistry.addHandlerAdapter(handlerAdapter);
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        this.handlerMappingRegistry.addHandlerMapping(handlerMapping);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        handlerMappingRegistry.initialize();
        super.init(config);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        final var handler = handlerMappingRegistry.getHandler(request);
        if (handler.isEmpty()) {
            try {
                response.setStatus(404);
                response.sendRedirect("/404.jsp");
                return;
            } catch (IOException e) {
                throw new RuntimeException("알맞은 핸들러가 없습니다.");
            }
        }
        final var handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler.get());
        ModelAndView modelAndView = handlerAdapter.handle(request, response, handler.get());
        modelAndView.render(request, response);
    }
}
