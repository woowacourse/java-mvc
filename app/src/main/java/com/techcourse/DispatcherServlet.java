package com.techcourse;

import com.interface21.webmvc.servlet.HandlerAdapter;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.ControllerHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutionHandlerAdapter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private ManualHandlerMapping manualHandlerMapping;
    private AnnotationHandlerMapping annotationHandlerMapping;
    private ControllerHandlerAdapter controllerHandlerAdapter;
    private HandlerExecutionHandlerAdapter handlerExecutionHandlerAdapter;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();
        annotationHandlerMapping = new AnnotationHandlerMapping("com.techcourse");
        annotationHandlerMapping.initialize();

        controllerHandlerAdapter = new ControllerHandlerAdapter();
        handlerExecutionHandlerAdapter = new HandlerExecutionHandlerAdapter();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            final Object handler = getHandler(request);
            if (handler == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            final HandlerAdapter handlerAdapter = getHandlerAdapter(handler);
            final ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);
            modelAndView.getView().render(modelAndView.getModel(), request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object getHandler(final HttpServletRequest request) {
        Object annotationHandler = annotationHandlerMapping.getHandler(request);
        if (annotationHandler != null) {
            return annotationHandler;
        }

        Object manualHandler = manualHandlerMapping.getHandler(request);
        if (manualHandler != null) {
            return manualHandler;
        }

        return null;
    }

    private HandlerAdapter getHandlerAdapter(Object handler) {
        if (handlerExecutionHandlerAdapter.supports(handler)) {
            return handlerExecutionHandlerAdapter;
        }

        if (controllerHandlerAdapter.supports(handler)) {
            return controllerHandlerAdapter;
        }

        throw new IllegalStateException("Handler adapter가 없습니다.: " + handler);
    }
}
