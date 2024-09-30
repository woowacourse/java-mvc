package com.interface21.webmvc.servlet;

import com.interface21.webmvc.ModelAndView;
import com.interface21.webmvc.adapter.HandlerAdapter;
import com.interface21.webmvc.adapter.RequestHandlerAdapter;
import com.interface21.webmvc.mapping.AnnotationHandlerMapping;
import com.interface21.webmvc.mapping.HandlerMapping;
import com.interface21.webmvc.view.View;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final String basePackage;
    private HandlerAdapter handlerAdapter;

    public DispatcherServlet(String basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    public void init() {
        List<HandlerMapping> handlerMappings = List.of(
                new AnnotationHandlerMapping(basePackage)
        );
        handlerMappings.forEach(mapping -> {
            try {
                mapping.initialize();
            } catch (Exception e) {
                log.error("Error during handler mapping initialization", e);
            }
        });
        handlerAdapter = new RequestHandlerAdapter(handlerMappings);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        final String method = request.getMethod();
        log.debug("Method : {}, Request URI : {}", method, requestURI);

        try {
            ModelAndView modelAndView = handlerAdapter.handle(request, response);
            move(modelAndView, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void move(
            final ModelAndView modelAndView, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        Map<String, Object> model = modelAndView.getModel();
        View view = modelAndView.getView();
        view.render(model, request, response);
    }
}
