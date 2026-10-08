package com.techcourse;

import com.interface21.webmvc.servlet.HandlerAdapter;
import com.interface21.webmvc.servlet.HandlerMapping;
import com.interface21.webmvc.servlet.ModelAndView;
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

    private final List<HandlerMapping> mappings;
    private final List<HandlerAdapter> adapters;

    public DispatcherServlet(final List<HandlerMapping> mappings, final List<HandlerAdapter> adapters) {
        this.mappings = List.copyOf(mappings);
        this.adapters = List.copyOf(adapters);
    }

    @Override
    public void init() {
        for (HandlerMapping mapping : mappings) {
            mapping.initialize();
        }
    }

    @Override
    protected void service(
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            Object handler = findHandler(request);
            HandlerAdapter adapter = findAdapter(handler);
            ModelAndView modelAndView = adapter.handle(request, response, handler);
            modelAndView.getView()
                    .render(modelAndView.getModel(), request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object findHandler(HttpServletRequest request) {
        for (HandlerMapping mapping : mappings) {
            Object handler = mapping.getHandler(request);
            if (handler != null) {
                return handler;
            }
        }
        throw new IllegalArgumentException("핸들러를 찾을 수 없습니다. " + request.getRequestURI());
    }

    private HandlerAdapter findAdapter(Object handler) {
        for (HandlerAdapter adapter : adapters) {
            if (adapter.supports(handler)) {
                return adapter;
            }
        }
        throw new IllegalArgumentException("어댑터를 찾을 수 없습니다. " + handler);
    }
}
