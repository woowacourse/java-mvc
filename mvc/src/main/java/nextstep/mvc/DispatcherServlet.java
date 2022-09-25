package nextstep.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.handleradapter.HandlerAdapter;
import nextstep.mvc.handleradapter.HandlerAdapterRegistry;
import nextstep.mvc.handlermapping.HandlerMapping;
import nextstep.mvc.handlermapping.HandlerMappingRegistry;
import nextstep.mvc.view.ModelAndView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegistry handlerMappings;
    private final HandlerAdapterRegistry handlerAdapters;

    public DispatcherServlet() {
        this.handlerMappings = new HandlerMappingRegistry();
        this.handlerAdapters = new HandlerAdapterRegistry();
    }

    @Override
    public void init() {
        handlerMappings.init();
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            final Object handler = handlerMappings.getHandler(request);
            final ModelAndView modelAndView = handlerAdapters.getModelAndView(handler, request, response);
            final Map<String, Object> model = modelAndView.getModel();
            modelAndView.getView().render(model, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
