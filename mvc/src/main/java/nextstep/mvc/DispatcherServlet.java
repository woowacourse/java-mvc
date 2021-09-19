package nextstep.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.adapter.HandlerAdapter;
import nextstep.mvc.exception.AbstractCustomException;
import nextstep.mvc.handler.HandlerMapping;
import nextstep.mvc.register.HandlerAdapterRegistry;
import nextstep.mvc.register.HandlerMappingRegistry;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.View;
import nextstep.mvc.view.ViewResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegistry handlerMappingRegistry;
    private final HandlerAdapterRegistry handlerAdapterRegistry;
    private final ViewResolver viewResolver = new ViewResolver();

    public DispatcherServlet() {
        this.handlerMappingRegistry = new HandlerMappingRegistry();
        this.handlerAdapterRegistry = new HandlerAdapterRegistry();
    }

    @Override
    public void init() {
        handlerMappingRegistry.init();
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException {
        LOG.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());
        ModelAndView modelAndView;
        try {
            modelAndView = getModelAndView(request, response);
            View view = viewResolver.resolverViewName(modelAndView.getViewName());
            view.render(modelAndView.getModel(), request, response);
        } catch (Throwable e) {
            LOG.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private ModelAndView getModelAndView(HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        try {
            Object handle = handlerMappingRegistry.getHandle(request);
            HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handle);
            return handlerAdapter.handle(request, response, handle);
        } catch (AbstractCustomException e) {
            return new ModelAndView(e.getPages().redirectPageName());
        }
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappingRegistry.addHandleMapping(handlerMapping);
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapterRegistry.addHandlerAdapter(handlerAdapter);
    }
}
