package com.techcourse;

import com.interface21.webmvc.servlet.HandlerAdapter;
import com.interface21.webmvc.servlet.HandlerMapping;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.ControllerHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutionAdapter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private List<HandlerMapping> handlerMappings;
    private List<HandlerAdapter> handlerAdapters;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();

        AnnotationHandlerMapping annotationHandlerMapping =
                new AnnotationHandlerMapping("com.techcourse.controller");
        annotationHandlerMapping.initialize();

        handlerMappings = List.of(annotationHandlerMapping, manualHandlerMapping);
        handlerAdapters = List.of(new ControllerHandlerAdapter(), new HandlerExecutionAdapter());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        try {
            Object handler = getHandler(request);

            if (handler == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            HandlerAdapter adapter = getHandlerAdapter(handler);
            ModelAndView modelAndView = adapter.handle(request, response, handler);

            modelAndView.getView().render(modelAndView.getModel(), request, response);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage(), e);
        }
    }

    private Object getHandler(HttpServletRequest request) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            Object handler = handlerMapping.getHandler(request);

            if (handler != null) {
                return handler;
            }
        }

        return null;
    }

    private HandlerAdapter getHandlerAdapter(Object handler) {
        for (HandlerAdapter adapter : handlerAdapters) {
            if (adapter.supports(handler)) {
                return adapter;
            }
        }

        throw new IllegalStateException(
                "핸들러를 실행할 어댑터가 없습니다: " + handler.getClass().getName()
        );
    }
}
