package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import com.interface21.webmvc.servlet.view.JspView;
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

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        final var manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();
        handlerMappings.add(manualHandlerMapping);

        final var annotationHandlerMapping = new AnnotationHandlerMapping("com.techcourse");
        annotationHandlerMapping.initialize();
        handlerMappings.add(annotationHandlerMapping);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            for (final HandlerMapping handlerMapping : handlerMappings) {
                final Object handler = handlerMapping.getHandler(request);
                if (handler == null) {
                    continue;
                }

                if (handler instanceof HandlerExecution handlerExecution) {
                    final ModelAndView modelAndView = handlerExecution.handle(request, response);
                    modelAndView.getView().render(modelAndView.getModel(), request, response);
                    return;
                }

                if (handler instanceof com.interface21.webmvc.servlet.mvc.asis.Controller controller) {
                    final var viewName = controller.execute(request, response);
                    new JspView(viewName).render(java.util.Map.of(), request, response);
                    return;
                }
            }
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
