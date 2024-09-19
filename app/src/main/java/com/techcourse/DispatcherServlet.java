package com.techcourse;

import java.util.ArrayList;
import java.util.List;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.asis.ManualHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final List<HandlerAdapter> adapters = new ArrayList<>();
    private final List<HandlerMapping> mappings = new ArrayList<>();

    public DispatcherServlet() {
        initHandlerAdapters();
        initHandlerMappings();
    }

    private void initHandlerMappings() {
        mappings.add(new ManualHandlerMapping());
        mappings.add(new AnnotationHandlerMapping("com.techcourse"));
    }

    private void initHandlerAdapters() {
        adapters.add(new ManualHandlerAdapter());
        adapters.add(new AnnotationHandlerAdapter());
    }

    @Override
    public void init() {
        mappings.forEach(HandlerMapping::initialize);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        String method = request.getMethod();
        String requestURI = request.getRequestURI();

        log.debug("Method : {}, Request URI : {}", method, requestURI);

        try {
            Object handler = getHandler(request);
            if (handlerNotFound(request, response, handler)) return;

            HandlerAdapter adapter = getHandlerAdapter(handler);
            ModelAndView mv = adapter.handle(request, response, handler);

            mv.render(request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private boolean handlerNotFound(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            ModelAndView mv = new ModelAndView(new JspView("/404.jsp"));
            mv.render(request, response);
            return true;
        }
        return false;
    }

    private Object getHandler(HttpServletRequest request) throws Exception {
        for (HandlerMapping mapping : mappings) {
            Object handler = mapping.getHandler(request);
            if (handler != null) {
                return handler;
            }
        }

        return null;
    }

    private HandlerAdapter getHandlerAdapter(Object handler) throws ServletException {
        for (HandlerAdapter adapter : adapters) {
            if (adapter.supports(handler)) {
                return adapter;
            }
        }

        throw new ServletException("HandlerAdapter not found");
    }
}
