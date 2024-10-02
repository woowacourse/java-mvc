package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final String basePackage;
    private CompositeHandlerMapping handlerMapping;

    public DispatcherServlet(String basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    public void init() {
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(basePackage);
        var annotationHandlerMappingAdaptor = new AnnotationHandlerMappingAdaptor(annotationHandlerMapping);

        this.handlerMapping = new CompositeHandlerMapping(annotationHandlerMappingAdaptor);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);
        try {
            HandlerAdaptor handler = handlerMapping.getHandler(request);
            ModelAndView modelAndView = handler.handle(request, response);
            render(modelAndView, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void render(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Map<String, Object> model = modelAndView.getModel();
        View view = modelAndView.getView();
        view.render(model, request, response);
    }
}
