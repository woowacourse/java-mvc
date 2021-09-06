package nextstep.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.tobe.HandlerAdapterRegister;
import nextstep.mvc.controller.tobe.HandlerMappingRegister;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegister handlerMappingRegister;
    private final HandlerAdapterRegister handlerAdapterRegister;

    public DispatcherServlet() {
        this.handlerMappingRegister = new HandlerMappingRegister();
        this.handlerAdapterRegister = new HandlerAdapterRegister();
    }

    @Override
    public void init() {
        handlerMappingRegister.init();
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappingRegister.addHandlerMapping(handlerMapping);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        LOG.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            final Object handler = handlerMappingRegister.getHandler(request);
            final HandlerAdapter handlerAdapter = handlerAdapterRegister.getHandlerAdapter(handler);
            final ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);
            render(modelAndView, request, response);
        } catch (Throwable e) {
            LOG.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void render(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        final View view = modelAndView.getView();
        view.render(modelAndView.getModel(), request, response);
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapterRegister.addHandlerAdapter(handlerAdapter);
    }
}
