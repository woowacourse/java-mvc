package nextstep.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegistry handlerMappings;
    private final HandlerAdapterRegistry handlerAdapters;

    public DispatcherServlet() {
        this.handlerMappings = new HandlerMappingRegistry(new ArrayList<>());
        this.handlerAdapters = new HandlerAdapterRegistry(new ArrayList<>());
    }

    @Override
    public void init() {
        handlerMappings.init();
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        this.handlerMappings.addHandlerMapping(handlerMapping);
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        this.handlerAdapters.addHandlerAdapter(handlerAdapter);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            Object handler = handlerMappings.getHandler(request);
            HandlerAdapter handlerAdapter = handlerAdapters.getHandlerAdapter(handler);
            ModelAndView mav = handlerAdapter.handle(request, response, handler);
            render(request, response, mav);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void render(final HttpServletRequest request, final HttpServletResponse response, final ModelAndView mav)
            throws Exception {
        View view = mav.getView();
        view.render(mav.getModel(), request, response);
    }
}
