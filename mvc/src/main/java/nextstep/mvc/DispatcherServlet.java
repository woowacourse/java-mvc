package nextstep.mvc;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegistry handlerMappingRegistry;
    private final List<HandlerAdapter> handlerAdapters;

    public DispatcherServlet() {
        handlerMappingRegistry = new HandlerMappingRegistry();
        this.handlerAdapters = new ArrayList<>();
    }

    @Override
    public void init() {
        handlerMappingRegistry.initialize();
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappingRegistry.addHandlerMapping(handlerMapping);
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            final Object handler = handlerMappingRegistry.getHandler(request);

            for (HandlerAdapter handlerAdapter : handlerAdapters) {
                if (handlerAdapter.supports(handler)) {
                    ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);
                    modelAndView.render(request, response);
                }
            }
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
