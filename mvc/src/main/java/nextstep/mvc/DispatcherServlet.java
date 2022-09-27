package nextstep.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.tobe.exception.ControllerNotFoundException;
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

    @Override
    public void init() {
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        handlerAdapterRegistry.addHandlerAdapter(handlerAdapter);
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMappingRegistry.addHandlerMapping(handlerMapping);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            Object controller = handlerMappingRegistry.getHandler(request)
                    .orElseThrow(ControllerNotFoundException::new);

            HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(controller);
            ModelAndView modelAndView = handlerAdapter.handle(request, response, controller);
            System.out.println("modelAndView = " + modelAndView);
            modelAndView.render(request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
