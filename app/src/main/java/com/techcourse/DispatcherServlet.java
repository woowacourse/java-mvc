package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.adapter.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.adapter.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.adapter.ManualHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.handler.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import jakarta.servlet.ServletException;
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

    private static final String BASE_PACKAGE = "com.techcourse";

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();
    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        handlerMappings.add(new AnnotationHandlerMapping(BASE_PACKAGE));
        handlerMappings.add(new ManualHandlerMapping());
        handlerMappings.forEach(HandlerMapping::initialize);

        handlerAdapters.add(new AnnotationHandlerAdapter());
        handlerAdapters.add(new ManualHandlerAdapter());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            Object handler = null;

            for (HandlerMapping handlerMapping : handlerMappings) {
                handler = handlerMapping.getHandler(request);
                if (handler != null) {
                    break;
                }
            }

            if (handler == null) {
                throw new RuntimeException("실행할 수 있는 핸들러가 없습니다.");
            }

            ModelAndView modelAndView = null;

            for (HandlerAdapter handlerAdapter : handlerAdapters) {
                if (!handlerAdapter.support(handler)) {
                    continue;
                }
                modelAndView = handlerAdapter.handle(request, response, handler);
                break;
            }

            if (modelAndView == null) {
                throw new RuntimeException("실행할 수 있는 Adapter가 없습니다.");
            }

            final var view = modelAndView.getView();
            final var model = modelAndView.getModel();
            view.render(model, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
