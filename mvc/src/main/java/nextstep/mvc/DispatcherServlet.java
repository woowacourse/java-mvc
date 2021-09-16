package nextstep.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.tobe.HandlerAdapters;
import nextstep.mvc.controller.tobe.HandlerMappings;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappings handlerMappings;
    private final HandlerAdapters handlerAdapters;

    public DispatcherServlet() {
        this.handlerMappings = new HandlerMappings();
        this.handlerAdapters = new HandlerAdapters();
    }

    @Override
    public void init() {
        handlerMappings.initialize();
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappings.addHandlerMapping(handlerMapping);
    }

    public void addHandlerAdaptor(HandlerAdapter handlerAdapter) {
        handlerAdapters.addHandlerAdapter(handlerAdapter);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            Object handler = handlerMappings.getHandler(request);

            HandlerAdapter handlerAdapter = handlerAdapters.getHandlerAdaptor(handler);

            ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);

            View view = modelAndView.getView();
            Map<String, Object> model = modelAndView.getModel();

            view.render(model, request, response);

        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
