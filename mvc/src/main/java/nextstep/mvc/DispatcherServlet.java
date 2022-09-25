package nextstep.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.View;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegistry handlerMappings;
    private final HandlerAdaptorRegistry handlerAdaptors;

    public DispatcherServlet(HandlerMappingRegistry handlerMappings, HandlerAdaptorRegistry handlerAdaptors) {
        this.handlerMappings = handlerMappings;
        this.handlerAdaptors = handlerAdaptors;
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMappings.addHandlerMapping(handlerMapping);
    }

    public void addHandlerAdaptors(final HandlerAdaptor handlerAdapter) {
        handlerAdaptors.addHandlerAdaptor(handlerAdapter);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws
        ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            process(request, response);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void process(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        Object handler = handlerMappings.getHandler(request);
        HandlerAdaptor handlerAdapter = handlerAdaptors.getHandlerAdaptor(handler);

        ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);
        View view = modelAndView.getView();
        view.render(modelAndView.getModel(), request, response);
    }
}
