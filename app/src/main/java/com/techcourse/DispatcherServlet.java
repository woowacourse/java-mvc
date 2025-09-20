package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.ControllerHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.ControllerScanner;
import com.interface21.webmvc.servlet.mvc.tobe.ExecutionHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();
    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();


    @Override
    public void init() {
        final String basePackage = "com.techcourse";
        ControllerScanner controllerScanner = new ControllerScanner(basePackage);

        List<HandlerMapping> mappings = List.of(
                new ManualHandlerMapping(),
                new AnnotationHandlerMapping(controllerScanner.scan())
        );
        this.handlerMappings.addAll(mappings);

        List<HandlerAdapter> adapters = List.of(
                new ControllerHandlerAdapter(),
                new ExecutionHandlerAdapter()
        );
        this.handlerAdapters.addAll(adapters);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) {
        try {
            Object handler = getHandler(request);
            HandlerAdapter handlerAdapter = getHandlerAdapter(handler);
            ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);
            View view = modelAndView.getView();
            view.render(modelAndView.getModel(), request, response);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private HandlerAdapter getHandlerAdapter(Object handler) {
        for (HandlerAdapter handlerAdapter : handlerAdapters) {
            if (handlerAdapter.supports(handler)) {
                return handlerAdapter;
            }
        }
        throw new IllegalStateException("no handlerAdapter");
    }

    private Object getHandler(HttpServletRequest request) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            return handlerMapping.getHandler(request);
        }
        throw new IllegalStateException("no handler ");
    }
}
