package nextstep.mvc;

import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegistry handlerMappingRegistry;
    private final HandlerExecutor handlerExecutor;

    public DispatcherServlet() {
        this.handlerMappingRegistry = new HandlerMappingRegistry();
        this.handlerExecutor = new HandlerExecutor();
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMappingRegistry.addHandlerMapping(handlerMapping);
    }

    public void addHandlerAdaptor(final HandlerAdapter handlerAdaptor) {
        handlerExecutor.addHandlerAdapter(handlerAdaptor);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());
        final Object handler = handlerMappingRegistry.getHandler(request)
            .orElseThrow(() -> new NoSuchElementException("handler not found"));
        final ModelAndView modelAndView = handlerExecutor.handle(request, response, handler);
        render(modelAndView, request, response);
    }

    private void render(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response) {
        modelAndView.render(request, response);
    }
}
