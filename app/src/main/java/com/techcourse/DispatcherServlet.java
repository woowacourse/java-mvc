package com.techcourse;

import java.util.Optional;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapters;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMappings;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerAdapters adapters;
    private HandlerMappings mappings;

    @Override
    public void init() {
        initHandlerAdapters();
        initHandlerMappings();
    }

    private void initHandlerAdapters() {
        this.adapters = new HandlerAdapters(new AnnotationHandlerAdapter());
    }

    private void initHandlerMappings() {
        String basePackage = getClass().getPackageName();
        this.mappings = new HandlerMappings(new AnnotationHandlerMapping(basePackage));
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
}
