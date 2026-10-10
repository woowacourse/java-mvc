package com.techcourse;

import com.interface21.webmvc.servlet.HandlerAdapter;
import com.interface21.webmvc.servlet.HandlerMapping;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.ControllerHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutionHandlerAdapter;
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

    private List<HandlerAdapter> handlerAdapters;
    private List<HandlerMapping> handlerMappings;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        handlerMappings = List.of(
                new AnnotationHandlerMapping("com.techcourse.controller"),
                new ManualHandlerMapping()
        );
        handlerMappings.forEach(HandlerMapping::initialize);

        handlerAdapters = List.of(
                new HandlerExecutionHandlerAdapter(),
                new ControllerHandlerAdapter()
        );
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
            final ModelAndView modelAndView = handlerAdapter.execute(request, response, handler);
            modelAndView.getView().render(modelAndView.getModel(), request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e);
        }
    }

    private Object getHandler(final HttpServletRequest request) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            final Object handler = handlerMapping.getHandler(request);
            if (handler != null) {
                return handler;
            }
        }
        return null;
    }

    private HandlerAdapter getHandlerAdapter(final Object handler) {
        for (HandlerAdapter handlerAdapter : handlerAdapters) {
            if (handlerAdapter.handleable(handler)) {
                return handlerAdapter;
            }
        }
        throw new IllegalStateException("핸들러를 처리할 어댑터가 없습니다: " + handler.getClass().getName());
    }
}
