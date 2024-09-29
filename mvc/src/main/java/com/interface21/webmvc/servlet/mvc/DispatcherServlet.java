package com.interface21.webmvc.servlet.mvc;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegistry handlerMappingRegistry;
    private final HandlerAdaptorRegistry handlerAdaptorRegistry;

    public DispatcherServlet() {
        this.handlerMappingRegistry = new HandlerMappingRegistry();
        this.handlerAdaptorRegistry = new HandlerAdaptorRegistry();
    }

    public void addHandlerAdaptor(HandlerAdaptor handlerAdaptor) {
        handlerAdaptorRegistry.addHandlerAdaptor(handlerAdaptor);
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappingRegistry.addHandlerMapping(handlerMapping);
    }

    @Override
    public void init() {
        addHandlerMapping(new ManualHandlerMapping());
        addHandlerMapping(new AnnotationHandlerMapping("com"));
        addHandlerAdaptor(new ControllerAdaptor());
        addHandlerAdaptor(new HandlerExecutionAdaptor());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            Object handler = handlerMappingRegistry.getHandler(request);
            HandlerAdaptor handlerAdaptor = handlerAdaptorRegistry.getHandlerAdaptor(handler);
            ModelAndView modelAndView = handlerAdaptor.handle(request, response, handler);
            render(modelAndView, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void render(ModelAndView modelAndview, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> model = modelAndview.getModel();
        View view = modelAndview.getView();

        view.render(model, request, response);
    }
}
