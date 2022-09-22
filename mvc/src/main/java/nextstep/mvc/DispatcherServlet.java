package nextstep.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.exception.ServletException;
import nextstep.mvc.handlerAdapter.HandlerAdapter;
import nextstep.mvc.handlerAdapter.HandlerAdapterRegistry;
import nextstep.mvc.handlerMapping.HandlerMapping;
import nextstep.mvc.handlerMapping.HandlerMappingRegistry;
import nextstep.mvc.view.ModelAndView;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegistry handlerMappingRegistry;
    private final HandlerAdapterRegistry handlerAdapterRegistry;

    public DispatcherServlet() {
        this.handlerMappingRegistry = new HandlerMappingRegistry();
        this.handlerAdapterRegistry = new HandlerAdapterRegistry();
    }

    @Override
    public void init() {
        handlerMappingRegistry.initialize();
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMappingRegistry.addHandlerMapping(handlerMapping);
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        handlerAdapterRegistry.addHandlerAdapter(handlerAdapter);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        final Object handler = handlerMappingRegistry.getHandler(request);
        final HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);

        render(request, response, handler, handlerAdapter);
    }

    private void render(final HttpServletRequest request, final HttpServletResponse response, final Object handler,
        final HandlerAdapter handlerAdapter) {
        try {
            final ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);
            modelAndView.render(request, response);
        } catch (Exception e) {
            throw new ServletException("render 과정에서 에러가 발생하였습니다.");
        }
    }
}
