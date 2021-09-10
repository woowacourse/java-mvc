package nextstep.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.adapter.HandlerAdapter;
import nextstep.mvc.adapter.HandlerAdapters;
import nextstep.mvc.mapping.HandlerMapping;
import nextstep.mvc.mapping.HandlerMappings;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        handlerMappings.initializeHandlerMappings();
    }

    public void addHandlerMappings(HandlerMapping... handlerMappings) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            this.handlerMappings.add(handlerMapping);
        }
    }

    public void addHandlerAdapters(HandlerAdapter... handlerAdapters) {
        for (HandlerAdapter handlerAdapter : handlerAdapters) {
            this.handlerAdapters.add(handlerAdapter);
        }
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            Object handler = handlerMappings.find(request);
            HandlerAdapter handlerAdapter = handlerAdapters.find(handler);
            ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);
            View view = modelAndView.getView();
            view.render(modelAndView.getModel(), request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
