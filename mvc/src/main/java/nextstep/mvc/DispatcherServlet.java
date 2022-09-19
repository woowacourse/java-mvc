package nextstep.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import nextstep.mvc.handleradaptor.HandlerAdapter;
import nextstep.mvc.handlermapping.HandlerMappingRegistry;
import nextstep.mvc.view.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegistry handlerMappingRegistry;
    private final List<HandlerAdapter> handlerAdapters;

    public DispatcherServlet(HandlerMappingRegistry handlerMappingRegistry) {
        this.handlerMappingRegistry = handlerMappingRegistry;
        this.handlerAdapters = new ArrayList<>();
    }

    @Override
    public void init() {
        handlerMappingRegistry.initialize();
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            final Object controller = handlerMappingRegistry.getHandler(request);
            final HandlerAdapter handlerAdapter = getHandlerAdapter(controller);
            final ModelAndView modelAndView = handlerAdapter.handle(request, response, controller);

            modelAndView.render(request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private HandlerAdapter getHandlerAdapter(Object controller) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(controller))
                .findFirst()
                .orElseThrow();
    }
}
