package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.mvc.HandlerMappingRegistry;
import com.interface21.webmvc.servlet.mvc.asis.ControllerHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.ControllerScanner;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private static final String APPLICATION_PACKAGE = "com.techcourse";
    private static final long serialVersionUID = 1L;

    private HandlerMappingRegistry handlerMappingRegistry;
    private HandlerAdapterRegistry handlerAdapterRegistry;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        final var controllerHandlerMapping = ControllerHandlerMappingInitializer.handle();
        controllerHandlerMapping.initialize();

        final var controllerScanner = new ControllerScanner(APPLICATION_PACKAGE);
        final var annotationHandlerMapping = new AnnotationHandlerMapping(controllerScanner);
        annotationHandlerMapping.initialize();

        handlerMappingRegistry = new HandlerMappingRegistry();
        handlerMappingRegistry.addHandlerMapping(controllerHandlerMapping);
        handlerMappingRegistry.addHandlerMapping(annotationHandlerMapping);

        final var controllerHandlerAdapter = new ControllerHandlerAdapter();
        final var annotationHandlerAdapter = new AnnotationHandlerAdapter();

        handlerAdapterRegistry = new HandlerAdapterRegistry();
        handlerAdapterRegistry.addHandlerAdapter(controllerHandlerAdapter);
        handlerAdapterRegistry.addHandlerAdapter(annotationHandlerAdapter);
    }

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse res) {
        final var optionalHandler = handlerMappingRegistry.getHandler(req);
        if (optionalHandler.isEmpty()) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            log.debug("Handler is not Found : {}", req.getRequestURI());
            return;
        }

        final var handler = optionalHandler.get();
        final var adapter = handlerAdapterRegistry.getAdapter(handler);

        try {
            final var modelAndView = adapter.handle(handler, req, res);
            render(modelAndView, req, res);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void render(final ModelAndView modelAndView,
                        final HttpServletRequest req,
                        final HttpServletResponse res) throws Exception {

        final var view = modelAndView.getView();
        view.render(modelAndView.getModel(), req, res);

    }
}
