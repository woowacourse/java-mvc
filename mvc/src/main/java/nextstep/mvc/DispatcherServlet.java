package nextstep.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import nextstep.mvc.view.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegistry handlerMappingRegistry;
    private final List<HandlerAdapter> adapters = List.of(
            new ControllerHandlerAdapter(),
            new AnnotationHandlerAdapter()
    );

    public DispatcherServlet() {
        handlerMappingRegistry = new HandlerMappingRegistry();
    }

    @Override
    public void init() {
        handlerMappingRegistry.init();
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMappingRegistry.addHandlerMapping(handlerMapping);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            final var controller = handlerMappingRegistry.getController(request);
            final var modelAndView = getModelAndView(controller, request, response);
            modelAndView.render(request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private ModelAndView getModelAndView(final Object handler, final HttpServletRequest request,
                                         final HttpServletResponse response) throws Exception {
        final HandlerAdapter adapter = getAdapter(handler);
        return adapter.handle(request, response, handler);
    }

    private HandlerAdapter getAdapter(final Object handler) {
        return adapters.stream()
                .filter(adapter -> adapter.supports(handler))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(handler.getClass().getName() + "는 처리할 수 없는 컨트롤러 타입 입니다.")
                );
    }
}
