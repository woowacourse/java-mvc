package com.interface21.webmvc.servlet.mvc;

import java.util.Optional;
import com.interface21.webmvc.servlet.mvc.adapter.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.adapter.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.adapter.HandlerAdapters;
import com.interface21.webmvc.servlet.mvc.mapping.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.mapping.HandlerMappings;
import com.interface21.webmvc.servlet.mvc.view.ModelAndView;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerAdapters adapters = new HandlerAdapters();
    private final HandlerMappings mappings = new HandlerMappings();

    @Override
    public void init() {
        mappings.initialize();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        String method = request.getMethod();
        String requestURI = request.getRequestURI();

        log.debug("Method : {}, Request URI : {}", method, requestURI);

        try {
            Optional<Object> handler = mappings.getHandler(request);
            if (handler.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            HandlerAdapter adapter = adapters.getHandlerAdapter(handler.get());
            ModelAndView mv = adapter.handle(request, response, handler.get());

            mv.render(request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    public void addHandlerMapping(AnnotationHandlerMapping annotationHandlerMapping) {
        mappings.addHandlerMapping(annotationHandlerMapping);
    }

    public void addHandlerAdapter(AnnotationHandlerAdapter annotationHandlerAdapter) {
        adapters.addHandlerAdapter(annotationHandlerAdapter);
    }
}
