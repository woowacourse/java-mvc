package nextstep.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.adapter.HandlerAdapter;
import nextstep.mvc.controller.adapter.HandlerAdapters;
import nextstep.mvc.controller.handler.HandlerMapping;
import nextstep.mvc.controller.handler.HandlerMappings;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappings handlerMappings;
    private final HandlerAdapters handlerAdapters;

    public DispatcherServlet() {
        handlerMappings = new HandlerMappings();
        handlerAdapters = new HandlerAdapters();
    }

    @Override
    public void init() {
        handlerMappings.initialize();
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        LOGGER.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            Object handler = handlerMappings.getHandlerMapping(request);
            HandlerAdapter handlerAdapter = handlerAdapters.getHandlerAdapter(handler);
            viewRender(request, response, handler, handlerAdapter);
        } catch (Exception e) {
            LOGGER.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void viewRender(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final HandlerAdapter handlerAdapter)
        throws Exception {
        ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);
        View view = modelAndView.getView();
        view.render(modelAndView.getModel(), request, response);
    }
}
