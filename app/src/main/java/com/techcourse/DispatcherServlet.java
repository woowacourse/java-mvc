package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.interface21.webmvc.servlet.view.JspView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);
    public static final String RESOURCES_BASE_PACKAGE = "com.techcourse";

    private final List<HandlerMapping> handlerMappings;

    public DispatcherServlet() {
        this.handlerMappings = new ArrayList<>();
    }

    @Override
    public void init() {
        HandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(RESOURCES_BASE_PACKAGE);
        HandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        annotationHandlerMapping.initialize();
        manualHandlerMapping.initialize();
        handlerMappings.add(annotationHandlerMapping);
        handlerMappings.add(manualHandlerMapping);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            ModelAndView modelAndView = executeController(request, response);
            if (modelAndView == null) {
                throw new IllegalArgumentException(String.format("Not found : %s", request.getRequestURI()));
            }

            move(modelAndView, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage());
        }
    }

    private ModelAndView executeController(HttpServletRequest request, HttpServletResponse response) {
        try {
            for (HandlerMapping handlerMapping : this.handlerMappings) {
                Object handler = handlerMapping.getHandler(request);

                if (handler == null) {
                    continue; // 다음 handlerMapping에 매핑될 수 있음.
                }
                if (handler instanceof HandlerExecution) {
                    return ((HandlerExecution) handler).handle(request, response);
                }
                if (handler instanceof Controller) {
                    return ((Controller) handler).execute(request, response);
                }
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void move(final ModelAndView modelAndView, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        View view = modelAndView.getView();
        String viewName = view.getViewName();
        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
            return;
        }

        Map<String, Object> model = modelAndView.getModel();
        view.render(model, request, response);
    }
}
