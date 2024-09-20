package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.view.JspView;
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

    private ManualHandlerMapping manualHandlerMapping;
    private AnnotationHandlerMapping annotationHandlerMapping;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();
        annotationHandlerMapping = new AnnotationHandlerMapping(getPackageName());
        annotationHandlerMapping.initialize();
    }

    private String getPackageName() {
        return getClass().getPackageName();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            serviceByManual(request, response, requestURI);
        } catch (NullPointerException e) {
            serviceByAnnotation(request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void serviceByManual(HttpServletRequest request, HttpServletResponse response, String requestURI)
            throws Exception {
        Controller controller = manualHandlerMapping.getHandler(requestURI);
        final var viewName = controller.execute(request, response);
        move(viewName, request, response);
    }

    private void move(final String viewName, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
            return;
        }

        final var requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }

    private void serviceByAnnotation(HttpServletRequest request, HttpServletResponse response) {
        HandlerExecution handlerExecution = (HandlerExecution) annotationHandlerMapping.getHandler(request);
        ModelAndView modelAndView = executeHandlerExecution(handlerExecution, request, response);
        move(modelAndView, request, response);
    }

    private ModelAndView executeHandlerExecution(HandlerExecution handlerExecution, HttpServletRequest request,
                                                 HttpServletResponse response) {
        try {
            return handlerExecution.handle(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void move(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> model = modelAndView.getModel();
        View view = modelAndView.getView();
        try {
            view.render(model, request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
