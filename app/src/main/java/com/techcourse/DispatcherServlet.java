package com.techcourse;

import java.util.Optional;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMappingRegistry;
import com.interface21.webmvc.servlet.mvc.tobe.ManualHandlerAdapter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerMappingRegistry handlerMappingRegistry;
    private HandlerAdapterRegistry handlerAdapterRegistry;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        initHandlerMappings();
        initHandlerAdapters();
    }

    private void initHandlerMappings() {
        handlerMappingRegistry = new HandlerMappingRegistry();

        ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();
        handlerMappingRegistry.addHandlerMapping(manualHandlerMapping);

        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(
                "com.techcourse.controller"
        );
        try {
            annotationHandlerMapping.initialize();
        } catch (Exception e) {
            log.error("annotationHandlerMapping initialize 실패", e);
        }
        handlerMappingRegistry.addHandlerMapping(annotationHandlerMapping);
    }

    private void initHandlerAdapters() {
        handlerAdapterRegistry = new HandlerAdapterRegistry();
        handlerAdapterRegistry.addHandlerAdapter(new ManualHandlerAdapter());
        handlerAdapterRegistry.addHandlerAdapter(new AnnotationHandlerAdapter());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            Optional<Object> handler = handlerMappingRegistry.getHandler(request);
            if (handler.isEmpty()) {
                log.error("handler 없음");
                return;
            }
            HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler.get());
            ModelAndView modelAndView = handlerAdapter.handle(request, response, handler.get());
            View view = modelAndView.getView();
            view.render(modelAndView.getModel(), request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
