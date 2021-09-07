package nextstep.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.adapter.HandlerAdapter;
import nextstep.mvc.adapter.HandlerAdapterRegistry;
import nextstep.mvc.adapter.HandlerMappingRegistry;
import nextstep.mvc.view.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final long serialVersionUID = 1L;

    @Override
    public void init() {
        HandlerMappingRegistry.initAll();
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        HandlerMappingRegistry.addHandlerMapping(handlerMapping);
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        HandlerAdapterRegistry.addHandlerAdapters(handlerAdapter);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        LOG.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            Object handler = getHandler(request);
            HandlerAdapter handlerAdapter = HandlerAdapterRegistry.getHandlerAdapter(handler);
            ModelAndView mv = handlerAdapter.handle(request, response, handler);
            mv.render(request, response);
        } catch (Exception e) {
            handleException(e);
        }
    }

    private void handleException(Exception e) throws ServletException {
        LOG.error("Exception : {}", e.getMessage(), e);
        throw new ServletException(e.getMessage());
    }

    private Object getHandler(HttpServletRequest request) {
        return HandlerMappingRegistry.getHandler(request).orElseThrow();
    }
}
